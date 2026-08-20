package paymentGateway.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data

@Entity
@Table(name="payment_order")
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phoneNumber;
    private String courseName;
    private Double amount;
    private String orderId;
    private String paymentId;
    private String status;
    private LocalDateTime createAt;

    public PaymentOrder(){//no Argument Constructor

    }

    //all arguments Constructor
    public PaymentOrder(Long id, String name, String email,
                        String phoneNumber, String courseName,
                        Double amount, String orderId, String paymentId,
                        String status, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.courseName = courseName;
        this.amount = amount;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.status = status;
        this.createAt = createAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentAt() {
        return createAt;
    }

    public void setPaymentAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
