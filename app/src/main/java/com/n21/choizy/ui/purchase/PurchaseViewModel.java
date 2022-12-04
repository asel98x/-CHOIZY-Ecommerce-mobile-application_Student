package com.n21.choizy.ui.purchase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.n21.choizy.model.Order;

public class PurchaseViewModel extends ViewModel {

    private MutableLiveData<String[]> payDetails;
    private MutableLiveData<Order> order;

    public PurchaseViewModel() {
        payDetails = new MutableLiveData<>();
        payDetails.setValue(null);
        order = new MutableLiveData<>();
        order.setValue(null);
    }

    public String[] getPaymentDetails() {
        return payDetails.getValue();
    }

    public void setPayDetails(String[] payDetails) {
        this.payDetails.setValue(payDetails);
    }

    public void setOrder(Order order) {
        this.order.setValue(order);
    }

    public Order getOrder(){
        return order.getValue();
    }

}
