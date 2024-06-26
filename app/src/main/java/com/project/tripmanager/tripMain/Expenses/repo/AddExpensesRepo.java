package com.project.tripmanager.tripMain.Expenses.repo;

import static com.project.tripmanager.newTrip.domain.CreateOrJoinGroup.BUDGET;
import static com.project.tripmanager.tripMain.Expenses.repo.ExpenseTypeRepo.EXPENSE_LIST;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.tripmanager.tripMain.ExpenseReports.dto.BudgetModel;
import com.project.tripmanager.tripMain.Expenses.domain.helpers.DateHelper;
import com.project.tripmanager.tripMain.Expenses.dto.ExpenditureModel;
import com.project.tripmanager.tripMain.Expenses.dto.ExpenseModel;
import com.project.tripmanager.tripMain.Expenses.dto.ExpensePathRef;
import com.project.tripmanager.tripMain.Expenses.dto.ExpenseTypeModel;
import com.project.tripmanager.tripMain.Expenses.dto.MemberExpenseModel;

import java.util.ArrayList;
import java.util.Objects;

public class AddExpensesRepo {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CollectionReference collRef;

    private static final String TAG = "AddExpensesRepo";
    public static final String EXPENSES = "Expenses";
    public static final String EXPENDITURE = "Expenditure";
    private String email,groupCode,typeId;
    private long amt;

    private final MutableLiveData<ExpensePathRef> ref = new MutableLiveData<>();
    private final ExpensePathRef expensePathRef = new ExpensePathRef();
    private final DateHelper dateHelper = new DateHelper();


    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
        collRef = null;
        expensePathRef.setGroupCode(groupCode);
        ref.setValue(expensePathRef);

    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
        expensePathRef.setTypeId(typeId);
        ref.setValue(expensePathRef);
    }

    public void addExpenses(String name, Long amt)
    {
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        this.amt = amt;
        assert email!=null;
        if(groupCode!=null && typeId!=null)
        {
             collRef = db.collection(groupCode).document(email).collection(EXPENSE_LIST)
                                            .document(typeId).collection(EXPENSES);
            DocumentReference docRef = collRef.document();
            ExpenseModel expense = new ExpenseModel();
            expense.setId(docRef.getId());
            expense.setName(name);
            expense.setAmt(amt);
            docRef.set(expense).addOnSuccessListener(unused -> {
                Log.d(TAG, "Expense added !");

                updateBudget(amt);

                //track the expenditure for the type
                fetchTypeExpense(amt);

            }).addOnFailureListener(e -> Log.d(TAG, e.toString()));
        }

    }

    private void updateBudget(Long amt) {
        db.collection(groupCode).document(BUDGET).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                BudgetModel budgetModel = documentSnapshot.toObject(BudgetModel.class);
                assert budgetModel != null;
                budgetModel.setCurrentAmt(budgetModel.getCurrentAmt() + amt);
                db.collection(groupCode).document(BUDGET).set(budgetModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Budget affected");
                    }
                });
            }
        });
    }


    private void fetchTypeExpense(long amt) {
        db.collection(groupCode).document(email).collection(EXPENSE_LIST)
                .document(typeId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ExpenseTypeModel expenseTypeModel = documentSnapshot.toObject(ExpenseTypeModel.class);
                        assert expenseTypeModel != null;
                        String prevDate = expenseTypeModel.getLastDate();
                        dateHelper.setPrevDate(prevDate);
                        int daysDiff = dateHelper.getTotalDiffInDays();
                        //if same day the add to that day's daily expense
                        if(daysDiff==0)
                        {
                            expenseTypeModel.setDaily(expenseTypeModel.getDaily() + amt);
                        }
                        // else reset that day to current day and daily expense to 0
                        else
                        {
                            expenseTypeModel.setLastDate(dateHelper.getCurrentDate());
                            expenseTypeModel.setDaily(amt);
                        }
                        //add the amount to the total overAll expenditure for that type
                        expenseTypeModel.setTotalExpense(expenseTypeModel.getTotalExpense() + amt);
                        updateExpenseType(expenseTypeModel);
                        Log.d(TAG, "diff: "+daysDiff+prevDate);

                        //add the expense globally in the expenditure document
                        getMemberName(expenseTypeModel);


                    }
                });
    }

    private void updateExpenseType(ExpenseTypeModel expenseTypeModel) {
        db.collection(groupCode).document(email).collection(EXPENSE_LIST)
                .document(typeId).set(expenseTypeModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.d(TAG, "Expense type updated!");
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, e.toString()));
    }




    private void getMemberName(ExpenseTypeModel expenseTypeModel) {
        db.collection(groupCode).document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String memberName  = documentSnapshot.getString("name");
                createOrUpdateExpenditure(expenseTypeModel,memberName);
            }
        });
    }

    private void createOrUpdateExpenditure(ExpenseTypeModel expenseTypeModel,String memberName) {

        db.collection(groupCode).document(EXPENDITURE).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    ExpenditureModel expenditureModel = documentSnapshot.toObject(ExpenditureModel.class);
                    assert expenditureModel != null;
                    updateExpenditure(expenditureModel,expenseTypeModel,memberName);
                }
                else
                {
                    Log.d(TAG, "new");
                    createExpenditure(expenseTypeModel,memberName);
                }
            }
        });

    }

    private void updateExpenditure(ExpenditureModel expenditureModel,ExpenseTypeModel expenseTypeModel, String memberName) {

       expenditureModel.setAllMembersExpenses(changeAllMembersExpenseList(expenditureModel,
                                                expenseTypeModel,memberName));

       expenditureModel.setAllTimeExpense(expenditureModel.getAllTimeExpense()+amt);

        String prevDate = expenditureModel.getCurrentDate();
        dateHelper.setPrevDate(prevDate);
        int daysDiff = dateHelper.getTotalDiffInDays();
        //if same day the add to that day's daily expense
        if(daysDiff==0)
        {
            expenditureModel.setDaily(expenditureModel.getDaily() + amt);

        }
        // else reset that day to current day and daily expense to amt
        else
        {
            expenditureModel.setCurrentDate(dateHelper.getCurrentDate());
            expenditureModel.setDaily(amt);
            expenditureModel.setDays(expenditureModel.getDays() + (long) daysDiff);
        }

        db.collection(groupCode).document(EXPENDITURE).set(expenditureModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Expenditure Model updated!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });



    }

    private ArrayList<MemberExpenseModel> changeAllMembersExpenseList(ExpenditureModel expenditureModel,ExpenseTypeModel expenseTypeModel,String memberName) {
        ArrayList<MemberExpenseModel> memberExpenseList = new ArrayList<>(expenditureModel.getAllMembersExpenses());
        boolean exists = false;
        boolean newDay = !dateHelper.getCurrentDate().equals(expenditureModel.getCurrentDate());

        if(newDay)
        {
            for (int i = 0; i < memberExpenseList.size(); i++) {
                memberExpenseList.get(i).setDaily(0L);
            }
        }

        for (int i = 0; i < memberExpenseList.size(); i++) {

            if (memberExpenseList.get(i).getEmail().equals(email)) {
                exists = true;
                memberExpenseList.get(i).setDaily(memberExpenseList.get(i).getDaily() + amt);
                memberExpenseList.get(i).setTotal(memberExpenseList.get(i).getTotal() + amt);

            }
        }
        if(!exists)
        {
            MemberExpenseModel memberExpense = new MemberExpenseModel();
            memberExpense.setMemberName(memberName);
            memberExpense.setDaily(expenseTypeModel.getDaily());
            memberExpense.setTotal(expenseTypeModel.getTotalExpense());
            memberExpense.setEmail(email);
            memberExpenseList.add(memberExpense);
        }
        return memberExpenseList;

    }

    private void createExpenditure(ExpenseTypeModel expenseTypeModel, String memberName) {
        MemberExpenseModel memberExpense = new MemberExpenseModel();
        memberExpense.setMemberName(memberName);
        memberExpense.setDaily(expenseTypeModel.getDaily());
        memberExpense.setTotal(expenseTypeModel.getTotalExpense());
        memberExpense.setEmail(email);

        ArrayList<MemberExpenseModel> memberExpenseList = new ArrayList<>();
        memberExpenseList.add(memberExpense);

        ExpenditureModel expenditureModel = new ExpenditureModel();
        expenditureModel.setAllTimeExpense(expenseTypeModel.getTotalExpense());
        expenditureModel.setDaily(expenseTypeModel.getDaily());
        expenditureModel.setDays(1L);
        expenditureModel.setCurrentDate(dateHelper.getCurrentDate());
        expenditureModel.setAllMembersExpenses(memberExpenseList);

        db.collection(groupCode).document(EXPENDITURE).set(expenditureModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Expenditure Model created!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });
    }


    public LiveData<ExpensePathRef> getPathRef() {
        return ref;
    }
}
