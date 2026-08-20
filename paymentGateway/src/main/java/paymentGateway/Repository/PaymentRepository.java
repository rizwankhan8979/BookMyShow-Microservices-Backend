package paymentGateway.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;
import paymentGateway.Entity.PaymentOrder;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentOrder, Long> {

    PaymentOrder findByOrderId(String orderId);

}
