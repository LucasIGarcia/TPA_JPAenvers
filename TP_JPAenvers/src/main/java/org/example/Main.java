package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;




public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("en marcha");

        try{
            entityManager.getTransaction().begin();

            Factura factura1 = Factura.builder()
                    .fecha("07/02/2020")
                    .numero(29)
                    .build();

            Domicilio dom = Domicilio.builder()
                    .nombreCalle("Perez")
                    .numero(912)
                    .build();
            Cliente cliente = Cliente.builder()
                    .dni(65)
                    .nombre("Juan")
                    .apellido("Garcia")
                    .build();
            cliente.setDomicilio(dom);
            dom.setCliente(cliente);

            factura1.setCliente(cliente);

            Categoria perecederos = Categoria.builder().denominacion("perecederos").build();
            Categoria lacteos = Categoria.builder().denominacion("lacteos").build();
            Categoria limpieza = Categoria.builder().denominacion("Limpieza").build();

            Articulo art1 = Articulo.builder().cantidad(5).denominacion("Yogurt").precio(20).build();
            Articulo art2 = Articulo.builder().cantidad(10).denominacion("Detergente").precio(50).build();

            art1.getCategorias().add(perecederos);
            art1.getCategorias().add(lacteos);
            lacteos.getArticulos().add(art1);
            perecederos.getArticulos().add(art1);

            art2.getCategorias().add(limpieza);
            limpieza.getArticulos().add(art2);

            DetalleFactura det1 = new DetalleFactura();
            det1.setArticulo(art1);
            det1.setCantidad(2);
            det1.setSubtotal(40);

            art1.getDetalle().add(det1);
            factura1.getDetalles().add(det1);
            det1.setFactura(factura1);

            DetalleFactura det2 = new DetalleFactura();
            det2.setArticulo(art2);
            det2.setCantidad(1);
            det2.setSubtotal(80);

            art2.getDetalle().add(det2);
            factura1.getDetalles().add(det2);
            det2.setFactura(factura1);

            factura1.setTotal(120);

            /*Factura factura1 = entityManager.find(Factura.class, 1L);
            factura1.setNumero(85);

            entityManager.remove(factura1);
            entityManager.merge(factura1);*/

            entityManager.persist(factura1);

            entityManager.flush();
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }



        // Cerrar el EntityManager y el EntityManagerFactory
        entityManager.close();
        entityManagerFactory.close();
    }
}