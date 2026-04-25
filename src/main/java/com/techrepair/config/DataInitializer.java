package com.techrepair.config;

import com.techrepair.model.*;
import com.techrepair.model.enums.ProductCategory;
import com.techrepair.model.enums.RepairStatus;
import com.techrepair.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final InventoryRepository inventoryRepository;
    private final InvoiceRepository invoiceRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createAdminUser();
        createClients();
        createProducts();
        createInventory();
        createRepairOrders();
        createInvoices();
        System.out.println("=== DataInitializer: All mock data loaded ===");
    }

    private void createAdminUser() {
        if (!userRepository.existsByEmail("admin@pcshop.com")) {
            User admin = User.builder()
                    .name("Carlos Martinez")
                    .email("admin@pcshop.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .active(true)
                    .build();
            userRepository.save(admin);
            System.out.println("Admin user created: admin@pcshop.com / admin123");
        }
    }

    private void createClients() {
        if (clientRepository.count() > 0) return;

        List<Client> clients = List.of(
            Client.builder().name("Juan Perez").email("juan.perez@gmail.com").phone("+54 11 5555-1234").address("Av. Santa Fe 1234, Buenos Aires").build(),
            Client.builder().name("Maria Gomez").email("maria.gomez@hotmail.com").phone("+54 11 5555-5678").address("Calle Florida 567, Buenos Aires").build(),
            Client.builder().name("Diego Fernandez").email("diego.fernandez@yahoo.com").phone("+54 11 5555-9012").address("Av. Corrientes 890, Buenos Aires").build(),
            Client.builder().name("Ana Rodriguez").email("ana.rodriguez@gmail.com").phone("+54 11 5555-3456").address("Calle Lavalle 234, Buenos Aires").build(),
            Client.builder().name("Lucas Sanchez").email("lucas.sanchez@outlook.com").phone("+54 11 5555-7890").address("Av. Rivadavia 3456, Buenos Aires").build()
        );
        clientRepository.saveAll(clients);
        System.out.println("Clients created: " + clients.size());
    }

    private void createProducts() {
        if (productRepository.count() > 0) return;

        List<Product> products = List.of(
            Product.builder().sku("GAM-001").name("PC Gamer Titan RTX").description("Potente PC para gaming con RTX 4090").category(ProductCategory.GAMING).specs("{\"CPU\":\"Intel Core i9-14900K\",\"GPU\":\"NVIDIA RTX 4090 24GB\",\"RAM\":\"64GB DDR5 6000MHz\",\"Almacenamiento\":\"2TB NVMe Gen5\",\"Fuente\":\"1200W 80+ Gold\"}").price(new BigDecimal("899999")).active(true).build(),
            Product.builder().sku("GAM-002").name("PC Gamer Phantom RTX 4070").description("PC gaming de alto rendimiento con RTX 4070").category(ProductCategory.GAMING).specs("{\"CPU\":\"AMD Ryzen 9 7900X\",\"GPU\":\"NVIDIA RTX 4070 Ti 12GB\",\"RAM\":\"32GB DDR5 5600MHz\",\"Almacenamiento\":\"1TB NVMe Gen4\",\"Fuente\":\"850W 80+ Gold\"}").price(new BigDecimal("549999")).active(true).build(),
            Product.builder().sku("OFF-001").name("PC Oficina Pro").description("Equipo ideal para trabajo de oficina y productividad").category(ProductCategory.OFFICE).specs("{\"CPU\":\"Intel Core i5-14400\",\"GPU\":\"NVIDIA GTX 1650 4GB\",\"RAM\":\"16GB DDR4 3200MHz\",\"Almacenamiento\":\"512GB NVMe Gen4\",\"Fuente\":\"550W 80+ Bronze\"}").price(new BigDecimal("189999")).active(true).build(),
            Product.builder().sku("OFF-002").name("PC Oficina Basic").description("Equipo economico para tareas basicas").category(ProductCategory.OFFICE).specs("{\"CPU\":\"AMD Ryzen 5 5600G\",\"GPU\":\"AMD Radeon Vega 7\",\"RAM\":\"16GB DDR4 3200MHz\",\"Almacenamiento\":\"256GB NVMe Gen3\",\"Fuente\":\"450W 80+ Bronze\"}").price(new BigDecimal("119999")).active(true).build(),
            Product.builder().sku("WS-001").name("Workstation Creator Pro").description("Estacion de trabajo para creadores de contenido").category(ProductCategory.WORKSTATION).specs("{\"CPU\":\"Intel Xeon W-2495X\",\"GPU\":\"NVIDIA RTX 4000 Ada 20GB\",\"RAM\":\"128GB DDR5 ECC\",\"Almacenamiento\":\"2TB NVMe Gen4 + 4TB HDD\",\"Fuente\":\"1000W 80+ Platinum\"}").price(new BigDecimal("1299999")).active(true).build(),
            Product.builder().sku("HE-001").name("PC High-End Ultimate").description("Lo mejor de lo mejor para usuarios exigentes").category(ProductCategory.HIGH_END).specs("{\"CPU\":\"AMD Ryzen 9 7950X3D\",\"GPU\":\"NVIDIA RTX 4090 24GB\",\"RAM\":\"128GB DDR5 6000MHz\",\"Almacenamiento\":\"4TB NVMe Gen5\",\"Fuente\":\"1600W 80+ Titanium\"}").price(new BigDecimal("1499999")).active(true).build()
        );
        productRepository.saveAll(products);
        System.out.println("Products created: " + products.size());
    }

    private void createInventory() {
        if (inventoryRepository.count() > 0) return;

        List<InventoryItem> items = List.of(
            InventoryItem.builder().sku("COMP-001").name("Procesador Intel Core i9-14900K").category("Componentes").brand("Intel").model("Core i9-14900K").quantity(5).minStockLevel(2).unitPrice(new BigDecimal("450000")).build(),
            InventoryItem.builder().sku("COMP-002").name("NVIDIA RTX 4090 24GB").category("GPU").brand("NVIDIA").model("RTX 4090").quantity(3).minStockLevel(1).unitPrice(new BigDecimal("1800000")).build(),
            InventoryItem.builder().sku("COMP-003").name("RAM DDR5 64GB 6000MHz").category("Memoria").brand("G.Skill").model("Trident Z5").quantity(8).minStockLevel(3).unitPrice(new BigDecimal("280000")).build(),
            InventoryItem.builder().sku("COMP-004").name("SSD NVMe 2TB Gen5").category("Almacenamiento").brand("Samsung").model("990 Pro").quantity(12).minStockLevel(5).unitPrice(new BigDecimal("220000")).build(),
            InventoryItem.builder().sku("COMP-005").name("Fuente 1200W 80+ Gold").category("Fuentes").brand("Corsair").model("RM1200x").quantity(4).minStockLevel(2).unitPrice(new BigDecimal("180000")).build(),
            InventoryItem.builder().sku("COMP-006").name("Gabinete gamer RGB").category("Gabinetes").brand("NZXT").model("H7 Flow").quantity(6).minStockLevel(2).unitPrice(new BigDecimal("95000")).build(),
            InventoryItem.builder().sku("COMP-007").name("Placa madre Z790").category("Componentes").brand("ASUS").model("ROG Maximus Z790").quantity(4).minStockLevel(2).unitPrice(new BigDecimal("380000")).build(),
            InventoryItem.builder().sku("COMP-008").name("Monitor 27\" 4K 144Hz").category("Perifericos").brand("LG").model("UltraGear 27GR93U").quantity(2).minStockLevel(3).unitPrice(new BigDecimal("450000")).build(),
            InventoryItem.builder().sku("COMP-009").name("Teclado mecanico RGB").category("Perifericos").brand("Logitech").model("G Pro X").quantity(15).minStockLevel(5).unitPrice(new BigDecimal("85000")).build(),
            InventoryItem.builder().sku("COMP-010").name("Mouse gaming Pro").category("Perifericos").brand("Razer").model("DeathAdder V3").quantity(18).minStockLevel(5).unitPrice(new BigDecimal("45000")).build()
        );
        inventoryRepository.saveAll(items);
        System.out.println("Inventory items created: " + items.size());
    }

    private void createRepairOrders() {
        if (repairOrderRepository.count() > 0) return;

        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();

        List<RepairOrder> orders = List.of(
            RepairOrder.builder()
                .client(clients.get(0)).deviceType("Laptop").deviceBrand("Dell").deviceModel("Inspiron 15")
                .serialNumber("SN-DELL-001").status(RepairStatus.IN_PROGRESS)
                .problemDescription("La laptop no enciende, parece un problema de alimentacion")
                .diagnosis("Fuente de alimentacion dañada").solution("Reemplazar fuente de alimentacion")
                .estimatedCost(new BigDecimal("25000")).createdAt(now.minusDays(5))
                .build(),
            RepairOrder.builder()
                .client(clients.get(1)).deviceType("PC de Escritorio").deviceBrand("ASUS").deviceModel("ROG Strix")
                .serialNumber("SN-ASUS-002").status(RepairStatus.PENDING)
                .problemDescription("Sobrecalentamiento durante sesiones de gaming")
                .estimatedCost(new BigDecimal("45000")).createdAt(now.minusDays(3))
                .build(),
            RepairOrder.builder()
                .client(clients.get(2)).deviceType("All-in-One").deviceBrand("HP").deviceModel("Envy 27")
                .serialNumber("SN-HP-003").status(RepairStatus.COMPLETED)
                .problemDescription("Pantalla con lineas verticales")
                .diagnosis("Cable flat defectuoso").solution("Reemplazo del cable flat y limpieza interna")
                .estimatedCost(new BigDecimal("18000")).finalCost(new BigDecimal("16500"))
                .createdAt(now.minusDays(10)).completedAt(now.minusDays(3))
                .build(),
            RepairOrder.builder()
                .client(clients.get(3)).deviceType("Laptop").deviceBrand("Lenovo").deviceModel("ThinkPad X1")
                .serialNumber("SN-LENOVO-004").status(RepairStatus.PENDING)
                .problemDescription("Teclado con teclas que no responden")
                .estimatedCost(new BigDecimal("12000")).createdAt(now.minusDays(1))
                .build(),
            RepairOrder.builder()
                .client(clients.get(4)).deviceType("PC de Escritorio").deviceBrand("MSI").deviceModel("Creator Z16")
                .serialNumber("SN-MSI-005").status(RepairStatus.CANCELLED)
                .problemDescription("Error en el disco rigido, ruidos extraños")
                .diagnosis("Disco rigido fallando").solution("Cliente decidio no reparar")
                .estimatedCost(new BigDecimal("35000")).createdAt(now.minusDays(7))
                .build(),
            RepairOrder.builder()
                .client(clients.get(0)).deviceType("Laptop").deviceBrand("HP").deviceModel("Pavilion 15")
                .serialNumber("SN-HP-006").status(RepairStatus.IN_PROGRESS)
                .problemDescription("Windows no arranca, pantalla azul")
                .diagnosis(" SSD fallando").solution("Reemplazar SSD y reinstalar Windows")
                .estimatedCost(new BigDecimal("35000")).createdAt(now.minusDays(2))
                .build(),
            RepairOrder.builder()
                .client(clients.get(2)).deviceType("PC de Escritorio").deviceBrand("AMD").deviceModel("Ryzen 7 5800X")
                .serialNumber("SN-AMD-007").status(RepairStatus.COMPLETED)
                .problemDescription("PC lenta, muchos procesos al inicio")
                .diagnosis("Malware detectado").solution("Limpieza profunda y antivirus")
                .estimatedCost(new BigDecimal("8000")).finalCost(new BigDecimal("7500"))
                .createdAt(now.minusDays(14)).completedAt(now.minusDays(12))
                .build()
        );
        repairOrderRepository.saveAll(orders);
        System.out.println("Repair orders created: " + orders.size());
    }

    private void createInvoices() {
        if (invoiceRepository.count() > 0) return;

        List<Client> clients = clientRepository.findAll();
        List<RepairOrder> completedOrders = repairOrderRepository.findByStatus(RepairStatus.COMPLETED);
        if (clients.isEmpty() || completedOrders.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();
        int invoiceNum = 1;

        List<Invoice> invoices = List.of(
            Invoice.builder()
                .invoiceNumber("INV-2024-" + String.format("%03d", invoiceNum++))
                .client(clients.get(2))
                .repairOrder(completedOrders.get(0))
                .paymentStatus("PAID")
                .subtotal(new BigDecimal("15000")).taxAmount(new BigDecimal("1500")).total(new BigDecimal("16500"))
                .issuedDate(now.minusDays(3))
                .build(),
            Invoice.builder()
                .invoiceNumber("INV-2024-" + String.format("%03d", invoiceNum++))
                .client(clients.get(2))
                .repairOrder(completedOrders.size() > 1 ? completedOrders.get(1) : null)
                .paymentStatus("PAID")
                .subtotal(new BigDecimal("6800")).taxAmount(new BigDecimal("680")).total(new BigDecimal("7480"))
                .issuedDate(now.minusDays(12))
                .build()
        );
        invoiceRepository.saveAll(invoices);
        System.out.println("Invoices created: " + invoices.size());
    }
}