package com.demo.demo.customer;

import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

//import static java.lang.StackStreamFactory.BATCH_SIZE;

@RestController
@RequestMapping("category")
public class CustomerController {

    @Autowired
    CustomerRepository repository;

    @Autowired
    InvalidCustomerRepository invalidCustomerRepository;

    @GetMapping("/fileCheck")    //
    public ResponseEntity fileCheck() throws IOException {

//        List<CustomerModel> userModels = new ArrayList<>();
        List<CustomerModel> validUserModels = new ArrayList<>();
        List<InvalidCustomerModel> inValidCustomerModels = new ArrayList<>();
        Set<CustomerModel> uniqueCustomer= new HashSet<>();
        String msg="";

        try {
            File f = new File("src/main/resources/file/1M-customers.txt");
            try (BufferedReader b = new BufferedReader(new FileReader(f))) {
                String readLine = "";
                // get total line count
                Instant lineCountStart = Instant.now();
                int lines = 0;

                Instant timeStart = Instant.now();
                System.out.println("Reading file using Buffered Reader");
                while ((readLine = b.readLine()) != null) {
                    lines++;
                    String array1[] = readLine.split("\\s*\\|\\s*");
                    CustomerModel model = new CustomerModel();
                    String name = array1[0];
                    String[] res = name.split("[,]", 0);
                    for (int i = 0; i < res.length; i++) {
                        switch (i) {
                            case 0:
                                model.setName(res[0]);
                                break;
                            case 1:
                                model.setBranch(res[1]);
                                break;
                            case 2:
                                model.setCity(res[2]);
                                break;
                            case 3:
                                model.setState(res[3]);
                                break;
                            case 4:
                                model.setZipCode(res[4]);
                                break;
                            case 5:
                                model.setPhone(res[5]);
                                break;
                            case 6:
                                model.setMail(res[6]);
                                break;
                            case 7:
                                model.setIp_number(res[7]);
                                break;
                        }

                    }

                    uniqueCustomer.add(model);
//                    userModels.add(model);
                }


                for (CustomerModel userModel : uniqueCustomer) {
                    if (isValidPhone(userModel.getPhone()) && isValidEmail(userModel.getMail())) {
                        validUserModels.add(userModel);
                    } else {
                        InvalidCustomerModel invalidCustomerModel = new InvalidCustomerModel(0, userModel.getName(),
                                userModel.getBranch(), userModel.getCity(), userModel.getState(),
                                 userModel.getZipCode(), userModel.getPhone(), userModel.getMail(), userModel.getIp_number());
//
                        inValidCustomerModels.add(invalidCustomerModel);
                    }
                }
                List<CustomerModel> customerModelList = repository.saveAll(validUserModels);
                List<InvalidCustomerModel> invalidCustomerModelList = invalidCustomerRepository.saveAll(inValidCustomerModels);
                writeValidCustomerDataToCsv();
                writeInValidCustomerDataToCsv();



                Instant timeEnd = Instant.now();
                long needTime = Duration.between(timeStart, timeEnd).toMillis();
                System.out.println("The Time Duration is: " + needTime + "ms");

                System.out.println("Total file line count: " + lines);
                Instant lineCountEnd = Instant.now();
                long timeElapsedLineCount = Duration.between(lineCountStart, lineCountEnd).toMillis();
                System.out.println("Line count time: " + timeElapsedLineCount + "ms");
                msg  = "The Time Duration is: " + needTime + "ms  /n  Total file line count: " + lines + "/n Line count time: " + timeElapsedLineCount + "ms";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().body("Data insert Successful /n"+msg);

    }


    public boolean isValidPhone(String phone) {
        // replace this regular expression with one that matches the format of your phone numbers
        String number = phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
        String regex = "^\\d{3}-\\d{3}-\\d{4}$";

        boolean result = number.matches(regex);
        return result;
    }

    public boolean isValidEmail(String email) {
        // replace this regular expression with one that matches the format of your email addresses
        String regex =  "^[A-Za-z0-9+_.-]+@(.+)$";
        boolean result =  email.matches(regex);
        return result;
    }


    @GetMapping("/validCustomer")
    public void exportAllValidCustomerToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=validCustomer" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);


        int counter = 1;
        List<CustomerModel> validCustomerModelList = repository.findAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "name",  "branch", "city", "state","zipCode","phone","mail","ip_number"};
        String[] nameMapping = {"id", "name", "branch", "city", "state","zipCode","phone","mail","ip_number"};

        csvWriter.writeHeader(csvHeader);

        for (CustomerModel user :validCustomerModelList ) {
            csvWriter.write(user, nameMapping);
        }

        csvWriter.close();

    }

    private static final int BATCH_SIZE = 10000;
    @GetMapping("/validCustomerSlab")
    public String writeValidCustomerDataToCsv() throws IOException {
        int counter = 1;
        int index = 0;
        List<CustomerModel> customerModelList = repository.findAll();
        List<CustomerModel> batch = new ArrayList<>();
        for (CustomerModel record : customerModelList) {
            batch.add(record);
            if (counter % BATCH_SIZE == 0) {
                writeBatchToCsv(batch, counter / BATCH_SIZE);
                batch.clear();
                index++;
            }
            counter++;
        }
        if (!batch.isEmpty()) {
            writeBatchToCsv(batch, counter / BATCH_SIZE + 1);
            index++;
        }
        return  index+" file are create on upload folder ";
    }
    private void writeBatchToCsv(List<CustomerModel> batch, int batchNumber) throws IOException {
        String fileName = "upload/validCustomer" + batchNumber + ".csv";
        CSVWriter writer = new CSVWriter(new FileWriter(fileName));
        String[] header = {"ID", "name",  "branch", "city", "state","zipCode","phone","mail","ip_number"};
        writer.writeNext(header);

        for (CustomerModel customer : batch) {
            String[] record = {Long.toString(customer.getId()), customer.getName() , customer.getBranch() , customer.getCity() , customer.getState()
                    , customer.getZipCode() , customer.getPhone() , customer.getMail() , customer.getIp_number() };
            writer.writeNext(record);
        }
        writer.close();

        /*response.setContentType("text/csv");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=validCustomer" + currentDateTime+index+ ".csv";
        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "name",  "branch", "city", "state","zipCode","phone","mail","ip_number"};
        String[] nameMapping = {"id", "name", "branch", "city", "state","zipCode","phone","mail","ip_number"};

        csvWriter.writeHeader(csvHeader);

        for (CustomerModel customerModel :batch ) {
            csvWriter.write(customerModel, nameMapping);
        }

        csvWriter.close();*/
    }



    @GetMapping("/inValidCustomerSlab")
    public String writeInValidCustomerDataToCsv() throws IOException {
        int counter = 1;
        List<InvalidCustomerModel> customerModelList2 = invalidCustomerRepository.findAll();
        List<InvalidCustomerModel> batch = new ArrayList<>();
        writeBatchToCsv2(customerModelList2);

        return counter+" file are create on upload folder ";
    }
    private void writeBatchToCsv2(List<InvalidCustomerModel> batch) throws IOException {
        String fileName = "upload/inValidCustomer" + 1 + ".csv";
        CSVWriter writer = new CSVWriter(new FileWriter(fileName));
        String[] header = {"ID", "name",  "branch", "city", "state","zipCode","phone","mail","ip_number"};
        writer.writeNext(header);

        for (InvalidCustomerModel customer : batch) {
            String[] record = {Long.toString(customer.getId()), customer.getName() , customer.getBranch() , customer.getCity() , customer.getState()
                    , customer.getZipCode() , customer.getPhone() , customer.getMail() , customer.getIp_number() };
            writer.writeNext(record);
        }
        writer.close();
    }

}
