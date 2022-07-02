package re.jmai.repository;

import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import re.jmai.bean.StatusPaymentEnum;
import re.jmai.entity.HelloAssoPaymentEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<HelloAssoPaymentEntity, String> {

    @Query("SELECT p FROM HelloAssoPaymentEntity p ORDER BY p.date DESC")
    List<HelloAssoPaymentEntity> findAll();

    @Query("SELECT p.id FROM HelloAssoPaymentEntity p")
    List<String> findAllIds();

    Optional<HelloAssoPaymentEntity> findById(String id);

    HelloAssoPaymentEntity save(@NotNull HelloAssoPaymentEntity payment);

    @Query("SELECT id " +
            "FROM HelloAssoPaymentEntity p " +
            "WHERE p.insertionDate < ?1")
    List<Integer> findIdsByInsertionDateBefore(@NotNull LocalDateTime date);

    @Modifying
    @Query("DELETE FROM HelloAssoPaymentEntity p WHERE p.id IN ?1")
    void deleteById(List<String> paymentIds);

    @Modifying
    @Query("DELETE FROM HelloAssoPaymentEntity p WHERE p.id = ?1")
    void deleteById(String paymentIds);

    @Query("SELECT p FROM HelloAssoPaymentEntity p WHERE p.status = ?1")
    List<Optional<HelloAssoPaymentEntity>> getByStatus(StatusPaymentEnum status);
}
