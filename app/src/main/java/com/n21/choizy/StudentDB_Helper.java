package com.n21.choizy;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.InputType;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.n21.choizy.model.Branch;
import com.n21.choizy.model.Cart;
import com.n21.choizy.model.Category;
import com.n21.choizy.model.Company;
import com.n21.choizy.model.Feedback;
import com.n21.choizy.model.Offer;
import com.n21.choizy.model.Order;
import com.n21.choizy.model.RecommendedAD;
import com.n21.choizy.model.Student;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;

public class StudentDB_Helper extends DBClass{

    static NumberFormat format;
    Calendar date = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
    public StudentDB_Helper(Context context) {
        super(context);
        this.format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(2);
        format.setCurrency(Currency.getInstance("LKR"));
    }

    public void textHolderchange(TextInputLayout textInputLayout){
        if(!textInputLayout.isActivated()){
            textInputLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
            textInputLayout.setEndIconDrawable(R.drawable.ic_check);
            textInputLayout.setActivated(true);
            textInputLayout.getEditText().setEnabled(true);
            textInputLayout.getEditText().requestFocus();
            textInputLayout.getEditText().setSelection(textInputLayout.getEditText().length());

        }else if(textInputLayout.isActivated()){
            textInputLayout.setActivated(false);
            textInputLayout.setEndIconDrawable(R.drawable.ic_edit);
            // editText.setEnabled(false);
            textInputLayout.getEditText().setInputType(InputType.TYPE_NULL);
        }

    }

    public static void setFistLog() {
        SharedPreferences userData = getContext().getSharedPreferences(ChoizySP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();
        // LoginDetails details = db.customerDetials(emailAddresss.getText().toString(),password.getText().toString());
        editor.putString("FirstLog","false");
        editor.apply();
    }

    public static void setStudentKey(String StudentKey) {
        SharedPreferences userData = getContext().getSharedPreferences(ChoizySP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();
        // LoginDetails details = db.customerDetials(emailAddresss.getText().toString(),password.getText().toString());
        editor.putString("StudentKey",StudentKey);
        editor.apply();
    }

    public static void setNotificationKey(String StudentKey) {
        SharedPreferences userData = getContext().getSharedPreferences(ChoizySP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();
        // LoginDetails details = db.customerDetials(emailAddresss.getText().toString(),password.getText().toString());
        editor.putString("NotificationKey",StudentKey);
        editor.apply();
    }

    public static String getNotificationKey(){
        SharedPreferences preferences = getContext().getSharedPreferences(ChoizySP, MODE_PRIVATE);

        return preferences.getString("NotificationKey",null);
    }

    public static String getFormattedPrice(double price){
        return format.format(price);
    }

    public static String getFirstLog(){
        SharedPreferences preferences = getContext().getSharedPreferences(ChoizySP, MODE_PRIVATE);

        return preferences.getString("FirstLog","");
    }

    public static String getStudentKey(){
        SharedPreferences preferences = getContext().getSharedPreferences(ChoizySP, MODE_PRIVATE);

        return preferences.getString("StudentKey",null);
    }

    public static void logout(){
        SharedPreferences userData = getContext().getSharedPreferences(ChoizySP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();
        // LoginDetails details = db.customerDetials(emailAddresss.getText().toString(),password.getText().toString());
        editor.remove("StudentKey");
        editor.apply();
    }

    public Query getUpcomingAds(){
       return myRef.child("UpcomingAD");
    }

    public Query getCategories(){
        return myRef.child(Category.class.getSimpleName());
    }

    public void removeEventListener(ValueEventListener listener){
        myRef.removeEventListener(listener);
    }

    public Query getCompanyList(){
        return myRef.child(Company.class.getSimpleName());
    }

    public Query getCompanyByCategory(String categoryName){
        return myRef.child(Company.class.getSimpleName()).orderByChild("company_category").equalTo(categoryName);
    }

    public Query getCompDetails(String companyName){
        return myRef.child(Company.class.getSimpleName()).child(companyName);
    }

    public Query getBranchList(){
        return myRef.child(Branch.class.getSimpleName());
    }

    public Query getBranchList(String CompanyID){
        return myRef.child(Branch.class.getSimpleName()).orderByChild("compID").equalTo(CompanyID);
    }

    public Query getBranchDetails(String branchId){
        return myRef.child(Branch.class.getSimpleName()).child(branchId);
    }

    public Query getOffer(String branchID){
        return myRef.child(Offer.class.getSimpleName()).orderByChild("branchID").equalTo(branchID);
    }

    public Query studentGetDetailsById(String id){
        return myRef.child("Student").orderByChild("student_id").equalTo(id);
    }

    public Query studentGetDetailsByKey(String key){
        return myRef.child("Student").child(key);
    }




    public Query getOfferByID(String offerID){
        return myRef.child(Offer.class.getSimpleName()).child(offerID);

    }

    public Query getCartId(){
        return myRef.child(Cart.class.getSimpleName()).child(getStudentKey());
    }

    public Query getOngoingOrder(){
        return myRef.child(Order.class.getSimpleName()+"Ongoing").orderByChild("studentKey").equalTo(getStudentKey());
    }

    public Query getHistoryOrder(){
        return myRef.child(Order.class.getSimpleName()+"History").orderByChild("studentKey").equalTo(getStudentKey());
    }

    public String genOrderID(){
        return myRef.child(Order.class.getSimpleName()+"Ongoing").push().getKey();
    }

    public Query getRecommended(){
        return myRef.child(RecommendedAD.class.getSimpleName());
    }

    public UploadTask uploadImage(Uri uri){
        return myStorage.child(Student.class.getSimpleName()).child(System.currentTimeMillis()+getFileExtension(uri)).putFile(uri);
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(uri));
    }

    //Void
    public Task<Void> AddCart(Offer offer,int qut){
        return myRef.child(Cart.class.getSimpleName()).child(getStudentKey()).child(offer.getBranchID()).child(offer.getOfferId()).child("Qut").setValue(qut);
    }

    public Task<Void> deleteImg(String url){
        return firebaseStorage.getReferenceFromUrl(url).delete();
    }

    public Task<Void> deleteCart(){
        return myRef.child(Cart.class.getSimpleName()).child(getStudentKey()).removeValue();
    }

//    public Task<Void> AddOrder(Order order,String id){
//
//        return myRef.child(Order.class.getSimpleName()+"Ongoing").child(id).setValue(order);
//    }

    public Task<Void> updateOrder(Order order){
        myRef.child("BranchSales").child(order.getBranchID()).child(dateFormat.format(date.getTime())).child(order.getOrderID()).setValue(order);
        myRef.child(Order.class.getSimpleName()+"Ongoing").child(order.getOrderID()).removeValue();
        return myRef.child(Order.class.getSimpleName()+"History").child(order.getOrderID()).setValue(order);
    }

    public Task<Void> AddOrderWithID(Order order,String id){

        return myRef.child(Order.class.getSimpleName()+"Ongoing").child(id).setValue(order);
    }

    public Task<Void> updateStudent(Student student){
        return myRef.child("Student").child(getStudentKey()).setValue(student);
    }

    public Task<Void> submitFeedBack(Feedback feedback){
        return myRef.child(Feedback.class.getSimpleName()).push().setValue(feedback);
    }






}
