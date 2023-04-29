package io.bootify.my_app.repos;

import io.bootify.my_app.domain.Shaurma;
import io.bootify.my_app.domain.ShaurmaOrder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShaurmaOrderRepository extends JpaRepository<ShaurmaOrder, Long> {

    ShaurmaOrder findFirstByShaurmaList(Shaurma shaurma);

}
