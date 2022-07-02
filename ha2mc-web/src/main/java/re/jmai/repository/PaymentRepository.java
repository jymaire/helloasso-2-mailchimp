package re.jmai.repository;

import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import re.jmai.bean.StatusPaymentEnum;
import re.jmai.entity.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, String> {

    @Query("SELECT p FROM Payment p ORDER BY p.date DESC")
    List<Payment> findAll();

    @Query("SELECT p.id FROM Payment p")
    List<String> findAllIds();

    Optional<Payment> findById(String id);

    Payment save(@NotNull Payment payment);

    @Query("SELECT id " +
            "FROM Payment p " +
            "WHERE p.insertionDate < ?1")
    List<Integer> findIdsByInsertionDateBefore(@NotNull LocalDateTime date);

    @Modifying
    @Query("DELETE FROM Payment p WHERE p.id IN ?1")
    void deleteById(List<Integer> paymentIds);

    @Modifying
    @Query("DELETE FROM Payment p WHERE p.id = ?1")
    void deleteById(int paymentIds);

    @Query("SELECT p FROM Payment p WHERE p.status = ?1")
    List<Optional<Payment>> getByStatus(StatusPaymentEnum status);
}
