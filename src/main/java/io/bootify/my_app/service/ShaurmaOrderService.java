package io.bootify.my_app.service;

import io.bootify.my_app.domain.Shaurma;
import io.bootify.my_app.domain.ShaurmaOrder;
import io.bootify.my_app.model.ShaurmaOrderDTO;
import io.bootify.my_app.repos.ShaurmaOrderRepository;
import io.bootify.my_app.repos.ShaurmaRepository;
import io.bootify.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ShaurmaOrderService {

    private final ShaurmaOrderRepository shaurmaOrderRepository;
    private final ShaurmaRepository shaurmaRepository;

    public ShaurmaOrderService(final ShaurmaOrderRepository shaurmaOrderRepository,
            final ShaurmaRepository shaurmaRepository) {
        this.shaurmaOrderRepository = shaurmaOrderRepository;
        this.shaurmaRepository = shaurmaRepository;
    }

    public List<ShaurmaOrderDTO> findAll() {
        final List<ShaurmaOrder> shaurmaOrders = shaurmaOrderRepository.findAll(Sort.by("id"));
        return shaurmaOrders.stream()
                .map((shaurmaOrder) -> mapToDTO(shaurmaOrder, new ShaurmaOrderDTO()))
                .toList();
    }

    public ShaurmaOrderDTO get(final Long id) {
        return shaurmaOrderRepository.findById(id)
                .map(shaurmaOrder -> mapToDTO(shaurmaOrder, new ShaurmaOrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ShaurmaOrderDTO shaurmaOrderDTO) {
        final ShaurmaOrder shaurmaOrder = new ShaurmaOrder();
        mapToEntity(shaurmaOrderDTO, shaurmaOrder);
        return shaurmaOrderRepository.save(shaurmaOrder).getId();
    }

    public void update(final Long id, final ShaurmaOrderDTO shaurmaOrderDTO) {
        final ShaurmaOrder shaurmaOrder = shaurmaOrderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(shaurmaOrderDTO, shaurmaOrder);
        shaurmaOrderRepository.save(shaurmaOrder);
    }

    public void delete(final Long id) {
        shaurmaOrderRepository.deleteById(id);
    }

    private ShaurmaOrderDTO mapToDTO(final ShaurmaOrder shaurmaOrder,
            final ShaurmaOrderDTO shaurmaOrderDTO) {
        shaurmaOrderDTO.setId(shaurmaOrder.getId());
        shaurmaOrderDTO.setShaurmaList(shaurmaOrder.getShaurmaList() == null ? null : shaurmaOrder.getShaurmaList().getId());
        return shaurmaOrderDTO;
    }

    private ShaurmaOrder mapToEntity(final ShaurmaOrderDTO shaurmaOrderDTO,
            final ShaurmaOrder shaurmaOrder) {
        final Shaurma shaurmaList = shaurmaOrderDTO.getShaurmaList() == null ? null : shaurmaRepository.findById(shaurmaOrderDTO.getShaurmaList())
                .orElseThrow(() -> new NotFoundException("shaurmaList not found"));
        shaurmaOrder.setShaurmaList(shaurmaList);
        return shaurmaOrder;
    }

}
