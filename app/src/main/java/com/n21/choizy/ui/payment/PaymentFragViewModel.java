package com.n21.choizy.ui.payment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.n21.choizy.model.Cart;
import com.n21.choizy.model.Order;

import java.util.ArrayList;

public class PaymentFragViewModel extends ViewModel {

    private MutableLiveData<String[]> mText;
    private MutableLiveData<Double> price;
    private MutableLiveData<ArrayList<Cart>> cartList;

    private MutableLiveData<String[]> PaymentDetails2;
    private MutableLiveData<Order> order;

    public PaymentFragViewModel() {
        order = new MutableLiveData<>();
        PaymentDetails2 = new MutableLiveData<>();
        mText = new MutableLiveData<>();
        price = new MutableLiveData<>();
        mText.setValue(null);
        cartList  = new MutableLiveData<>();
        cartList.setValue(new ArrayList<>());
    }

    public Order getOrder(){
        return order.getValue();
    }

    public void SetOrder(Order order){
        this.order.setValue(order);
    }

    public LiveData<String[]> getPaymentDetails2() {
        return PaymentDetails2;
    }

    public void setPaymentDetails2(String[] payD){
        this.PaymentDetails2.setValue(payD);
    }

    public Double getPrice() {
        return price.getValue();
    }

    public void setPrice(Double price) {
        this.price.setValue(price);
    }

    public LiveData<String[]> getPaymentDetails() {
        return mText;
    }

    public LiveData<ArrayList<Cart>> getCartList() {
        return cartList;
    }

    public ArrayList<Cart> getCartListN() {
        return cartList.getValue();
    }

    public void setCartList(ArrayList<Cart> cartList) {
        this.cartList.setValue(cartList);
    }

    public void addCart(Cart cart){
        cartList.getValue().add(cart);
    }

    public void setmText(String[] payment) {
        this.mText.setValue(payment);
    }
}
