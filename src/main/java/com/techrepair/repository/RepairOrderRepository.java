package com.techrepair.repository;

import com.techrepair.model.RepairOrder;
import com.techrepair.model.enums.RepairStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {
    List<RepairOrder> findByStatus(RepairStatus status);
    List<RepairOrder> findByClientId(Long clientId);
}