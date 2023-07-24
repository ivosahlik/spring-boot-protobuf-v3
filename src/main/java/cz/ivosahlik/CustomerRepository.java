package cz.ivosahlik;


interface CustomerRepository {

    CustomerProtos.Customer findById(int id);

    CustomerProtos.CustomerList findAll();

}
