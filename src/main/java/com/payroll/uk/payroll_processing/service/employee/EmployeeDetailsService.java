package com.payroll.uk.payroll_processing.service.employee;

import com.payroll.uk.payroll_processing.dto.BankDetailsDTO;
import com.payroll.uk.payroll_processing.dto.employeedto.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.dto.mapper.EmployeeDetailsDTOMapper;
import com.payroll.uk.payroll_processing.entity.BankDetails;
import com.payroll.uk.payroll_processing.entity.TaxThreshold;
import com.payroll.uk.payroll_processing.entity.employee.EmployeeDetails;
import com.payroll.uk.payroll_processing.entity.employer.EmployerDetails;
import com.payroll.uk.payroll_processing.exception.*;
import com.payroll.uk.payroll_processing.repository.BankDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployeeDetailsRepository;
import com.payroll.uk.payroll_processing.repository.EmployerDetailsRepository;
import com.payroll.uk.payroll_processing.service.TaxThresholdService;
import com.payroll.uk.payroll_processing.service.ValidateData;
import com.payroll.uk.payroll_processing.utils.TaxPeriodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EmployeeDetailsService {
    private static final Logger logging= LoggerFactory.getLogger("EmployeeDetailsService.class");
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private  final EmployeeDetailsDTOMapper employeeDetailsDTOMapper;
    private  final BankDetailsRepository bankDetailsRepository;
    private final EmployerDetailsRepository employerDetailsRepository;
    private final TaxThresholdService taxThresholdService;
    private  final ValidateData validateData;

    public EmployeeDetailsService(EmployeeDetailsRepository employeeDetailsRepository,
                                  EmployeeDetailsDTOMapper employeeDetailsDTOMapper,BankDetailsRepository bankDetailsRepository, EmployerDetailsRepository employerDetailsRepository,
                                  TaxThresholdService taxThresholdService,ValidateData validateData) {
        this.employeeDetailsRepository = employeeDetailsRepository;
        this.employeeDetailsDTOMapper = employeeDetailsDTOMapper;
        this.bankDetailsRepository = bankDetailsRepository;
        this.employerDetailsRepository = employerDetailsRepository;
        this.taxThresholdService = taxThresholdService;
        this.validateData = validateData;
    }
    // Save employee details
    public EmployeeDetailsDTO saveEmployeeDetails(EmployeeDetailsDTO employeeDetailsDTO){
        if (employeeDetailsDTO.getEmployeeId()==null || employeeDetailsDTO.getEmployeeId().isEmpty()) {
            throw new DataValidationException("Employee ID cannot be null or empty");
        }
        if(employeeDetailsRepository.existsByEmail(employeeDetailsDTO.getEmail())){
            throw new DataValidationException("Employee with this email already exists");
        }
        if(employerDetailsRepository.existsByEmployerEmail(employeeDetailsDTO.getEmail())){
            throw new DataValidationException("Employee email already exists as Employer email");
        }
        if(employeeDetailsRepository.findByEmployeeId(employeeDetailsDTO.getEmployeeId()).isPresent()){
            throw new DataValidationException("Employee with this ID already exists");
        }
        if (employerDetailsRepository.findByEmployerId(employeeDetailsDTO.getEmployeeId()).isPresent()){
            throw new DataValidationException("Employee Id already exists as Employer ID");
        }
//        if (!employerDetailsRepository.existsByEmployerId( employeeDetailsDTO.getEmployerId())) {
//            throw new EmployerRegistrationException("Employer with this ID does not exist");
//        }
        if (employeeDetailsRepository.existsByNationalInsuranceNumber(employeeDetailsDTO.getNationalInsuranceNumber())){
            throw new DataValidationException("Employee with this National Insurance Number already exists");
        }
        validateData.validateEmployeeDetailsData(employeeDetailsDTO);
        String payrollReference = employerDetailsRepository.findByPayrollGivingRef();
//        if (payrollReference == null || payrollReference.isEmpty()) {
//            throw new IllegalArgumentException("Payroll Giving Reference not found for the employer");
//        }
         String lastPayrollId = employeeDetailsRepository.findLastEmployeePayrollId()
                .stream()
                .findFirst()
                .orElse(null);

        String currentPayrollId;
        if (lastPayrollId==null || lastPayrollId.isEmpty()){
            payrollReference=payrollReference==null?"T_F01":payrollReference;
            currentPayrollId= generateNextPayrollId(payrollReference);
        }
        else {
            currentPayrollId=generateNextPayrollId(lastPayrollId);
        }
        EmployeeDetails employeeData = employeeDetailsDTOMapper.mapToEmployeeDetails(employeeDetailsDTO);

        employeeData.setTotalPersonalAllowance(taxThresholdService.getPersonalAllowance(employeeData.getTaxYear()));
        employeeData.getOtherEmployeeDetails().setRemainingPersonalAllowance(employeeData.getTotalPersonalAllowance().subtract(employeeData.getPreviouslyUsedPersonalAllowance()));
         employeeData.setPayrollId(currentPayrollId);
//        bankDetailsRepository.save(employeeData.getBankDetails());
        EmployeeDetails savedEmployeeDetails = employeeDetailsRepository.save(employeeData);
        logging.info("successfully saved employee details with ID: {}", savedEmployeeDetails.getEmployeeId());

        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(savedEmployeeDetails);
    }

    // Get employee details by employee ID
    public EmployeeDetailsDTO getEmployeeDetailsByEmployeeId(String employeeId){
        if (employeeId == null || employeeId.isEmpty()) {
            throw new DataValidationException("Employee ID cannot be null or empty "+employeeId);
        }

        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + employeeId + " not found"));
        logging.info("successfully fetched employee details with ID: {}", employeeId);
        if (employeeDetails==null){
            throw  new ResourceNotFoundException("Employee details not found for ID: " + employeeId);
        }
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(employeeDetails);
    }
    public List<EmployeeDetailsDTO> getAllEmployeeDetails() {
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        List<EmployeeDetailsDTO> employeeDetailsListedData = employeeDetailsList.stream().map(employeeDetailsDTOMapper::mapToEmployeeDetailsDTO).toList();
        if(employeeDetailsListedData.isEmpty()) {
            throw new ResourceNotFoundException("No employee details found ");
        }
        logging.info("successfully fetched all employee details");
        return employeeDetailsListedData;
    }
// Get employee details by email
    public EmployeeDetailsDTO getEmployeeDetailsByEmail(String email){
        if (email == null || email.isEmpty()) {
            throw new DataValidationException("Email cannot be null or empty "+email);
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        logging.info("successfully fetched employee details with email: {}", email);
        if (employeeDetails==null){
            throw  new ResourceNotFoundException("Employee details not found for ID: " + email);
        }
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(employeeDetails);
    }
    // Update employee details by ID
    public EmployeeDetailsDTO updateEmployeeDetailsById(Long id,EmployeeDetailsDTO employeeDetailsDTO){
        if (employeeDetailsDTO.getEmployeeId() == null || employeeDetailsDTO.getEmployeeId().isEmpty()) {
            throw new ResourceNotFoundException("Employee ID or Id cannot be null or empty");
        }
        validateData.validateEmployeeDetailsData(employeeDetailsDTO);

        EmployeeDetails existingEmployeeDetails = employeeDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeDetailsDTO.getEmployeeId()));

        EmployeeDetails updatedEmployeeDetails = employeeDetailsDTOMapper.mapToEmployeeDetails(employeeDetailsDTO);
        updatedEmployeeDetails.getBankDetails().setId(existingEmployeeDetails.getBankDetails().getId());

        updatedEmployeeDetails.setId(existingEmployeeDetails.getId()); // Preserve the existing ID
        updatedEmployeeDetails.setPayrollId(existingEmployeeDetails.getPayrollId());
        EmployeeDetails savedEmployeeDetails = employeeDetailsRepository.save(updatedEmployeeDetails);
        logging.info("successfully updated employee details with ID: {}", savedEmployeeDetails.getEmployeeId());
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(savedEmployeeDetails);
    }

    //Update employee details by employee ID
    public EmployeeDetailsDTO updateEmployeeDetailsByEmployeeId(EmployeeDetailsDTO employeeDetailsDTO) {
        if (employeeDetailsDTO.getEmployeeId() == null || employeeDetailsDTO.getEmployeeId().isEmpty()) {
            throw new ResourceNotFoundException("Employee ID cannot be null or empty");
        }
//        if (!employerDetailsRepository.existsByEmployerId(employeeDetailsDTO.getEmployerId())) {
//            throw new IllegalArgumentException("Employer with this ID does not exist");
//        }
        EmployeeDetails existingEmployeeDetails = employeeDetailsRepository.findByEmployeeId(employeeDetailsDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeDetailsDTO.getEmployeeId()));

        EmployeeDetails updatedEmployeeDetails = employeeDetailsDTOMapper.mapToEmployeeDetails(employeeDetailsDTO);
//        updatedEmployeeDetails.setId(existingEmployeeDetails.getId()); // Preserve the existing ID
        EmployeeDetails savedEmployeeDetails = employeeDetailsRepository.save(updatedEmployeeDetails);
        return employeeDetailsDTOMapper.mapToEmployeeDetailsDTO(savedEmployeeDetails);
    }

    //Update employee Bank details by id
    public BankDetailsDTO updateBankDetailsById(Long id,BankDetailsDTO  bankDetailsDTO){
        if(id==null){
            throw new DataValidationException("Bank details ID cannot be null");
        }
        if (bankDetailsDTO.getAccountNumber() == null || bankDetailsDTO.getAccountNumber().isEmpty()) {
            throw new DataValidationException("Account Number cannot be null or empty");
        }
        if (bankDetailsDTO.getAccountName() == null || bankDetailsDTO.getAccountName().isEmpty()) {
            throw new DataValidationException("Account Name cannot be null or empty");
        }
//        if (bankDetailsDTO.getSortCode() == null || bankDetailsDTO.getSortCode().isEmpty()) {
//            throw new IllegalArgumentException("Sort Code cannot be null or empty");
//        }
        BankDetails bankDetails = bankDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank details not found with ID: " + id));
        bankDetails.setAccountNumber(bankDetailsDTO.getAccountNumber());
        bankDetails.setAccountName(bankDetailsDTO.getAccountName());
        bankDetails.setSortCode(bankDetailsDTO.getSortCode());
        bankDetails.setBankName(bankDetailsDTO.getBankName());
        bankDetails.setBankAddress(bankDetailsDTO.getBankAddress());
        bankDetails.setBankPostCode(bankDetailsDTO.getBankPostCode());
//        bankDetails.setTelephone(bankDetailsDTO.getTelephone());
//        bankDetails.setPaymentReference(bankDetailsDTO.getPaymentReference());
//        bankDetails.setIsRTIReturnsIncluded(bankDetailsDTO.getIsRTIReturnsIncluded());
//        bankDetails.setPaymentLeadDays(bankDetailsDTO.getPaymentLeadDays());
        bankDetails.setId(bankDetails.getId());

        return employeeDetailsDTOMapper.mapToBanKDetailsDTO(bankDetailsRepository.save(bankDetails));

    }

    public EmployeeDetailsDTO updateEmployeeOtherDetailsById(String employeeId){
       if(employeeId.isEmpty()|| employeeId == null) {
            throw new DataValidationException("Employee ID cannot be null or empty");
       }
        EmployeeDetails existingEmployeeDetails = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        return null;


    }

    // Delete employee details by ID
    public Boolean deleteEmployeeDetailsById(Long id){
        if (id == null) {
            throw new ResourceNotFoundException("Id of Employee cannot be null");
        }

        EmployeeDetails employeeDetails = employeeDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
         employeeDetailsRepository.delete(employeeDetails);
        logging.info("successfully deleted employee details with ID: {}", id);
        return true;
    }
    // Delete employee details by employee ID
    public Boolean deleteEmployeeDetailsByEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isEmpty()) {
            throw new ResourceNotFoundException("Employee ID cannot be null or empty");
        }
        EmployeeDetails employeeDetails = employeeDetailsRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Id not found with ID: " + employeeId));
        employeeDetailsRepository.delete(employeeDetails);
        return true;
    }


    public List<EmployeeDetailsDTO> getAllActiveEmployees() {
        List<EmployeeDetails> activeEmployees = employeeDetailsRepository.findActiveEmployees();
        if (activeEmployees.isEmpty()) {
            throw new ResourceNotFoundException("No active employees found");
        }
        logging.info("successfully fetched all active employees");
        return activeEmployees.stream()
                .map(employeeDetailsDTOMapper::mapToEmployeeDetailsDTO)
                .collect(Collectors.toList());
    }
    public List<EmployeeDetailsDTO> getAllInActiveEmployees() {
        List<EmployeeDetails> inactiveEmployees = employeeDetailsRepository.findInactiveEmployees();
        if (inactiveEmployees.isEmpty()) {
            throw new ResourceNotFoundException("No inactive employees found");
        }
        logging.info("successfully fetched all inactive employees");
        return inactiveEmployees.stream()
                .map(employeeDetailsDTOMapper::mapToEmployeeDetailsDTO)
                .collect(Collectors.toList());
    }
    public List<EmployeeDetailsDTO> getAllReadyForLeavingEmployees() {
        List<EmployeeDetails> readyForLeavingEmployees = employeeDetailsRepository.findReadyForLeavingEmployees();
        if (readyForLeavingEmployees.isEmpty()) {
            throw new ResourceNotFoundException("No employees ready for leaving found");
        }
        logging.info("successfully fetched all employees ready for leaving");
        return readyForLeavingEmployees.stream()
                .map(employeeDetailsDTOMapper::mapToEmployeeDetailsDTO)
                .collect(Collectors.toList());
    }

    //pension contributions auto enrollment
    public boolean isEligibleForAutoEnrollment(BigDecimal annualIncome, LocalDate dateOfBirth, String taxYear, EmployeeDetails.Gender gender) {
        if (dateOfBirth == null || annualIncome == null ||taxYear==null||gender==null) {
            throw new DataValidationException("Invalid input: Data cannot be null");
        }
        if (annualIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new DataValidationException("Annual income cannot be negative");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new DataValidationException("Date of birth cannot be in the future");
        }

        // 1. Calculate age from DOB
        int employeeAge = TaxPeriodUtils.calculateAge(dateOfBirth);

        // 2. Get auto-enrolment threshold from tax thresholds service
        BigDecimal autoEnrolmentThreshold = taxThresholdService.getAllowanceData(
               taxYear,
                TaxThreshold.BandName.AUTO_ENROLMENT_PENSION_CONTRIBUTION
        );

        if (autoEnrolmentThreshold == null) {
            throw new DataValidationException("Auto-enrolment threshold not configured for tax year: " + taxYear);
        }

        // 3. Get employer details (ensure it's not null and has valid retirement ages)
        EmployerDetails employerData = employerDetailsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new DataValidationException("Employer details not found"));

        int retirementAge = getRetirementAgeBasedOnGender(employerData, String.valueOf(gender));

        logging.info("Employee Age: {}, Auto-Enrolment Threshold: {}, Retirement Age: {}", employeeAge, autoEnrolmentThreshold, retirementAge);
        // 4. Evaluate eligibility: age, salary, and below retirement age
        return annualIncome.compareTo(autoEnrolmentThreshold) > 0
                && employeeAge >= 22
                && employeeAge < retirementAge;
    }


    private int getRetirementAgeBasedOnGender(EmployerDetails employer, String gender) {
        return switch (gender.toUpperCase()) {
            case "MALE" -> employer.getTerms().getMaleRetirementAge();
            case "FEMALE" -> employer.getTerms().getFemaleRetirementAge();
            default -> throw new DataValidationException("Unknown gender: " + gender);
        };
    }





    public String extractPrefix(String payrollId) {
        if (payrollId == null || payrollId.isEmpty()) {
            throw new IllegalArgumentException("Payroll ID cannot be null or empty");
        }
        return payrollId.replaceAll("\\d+$", ""); // removes all digits from end
    }

    public String generateNextPayrollId(String lastPayrollId) {
        if (lastPayrollId == null || lastPayrollId.isEmpty()) {
            throw new IllegalArgumentException("Payroll ID cannot be null or empty");
        }

        // Match trailing digits (e.g., EMP12X02 -> prefix=EMP12X, numberPart=02)
        Pattern pattern = Pattern.compile("^(.*?)(\\d+)?$");
        Matcher matcher = pattern.matcher(lastPayrollId);

        if (matcher.matches()) {
            String prefix = matcher.group(1);
            String numberStr = matcher.group(2);

            int number = 1; // default start
            int paddingLength = 2; // format like 01, 02, etc.

            if (numberStr != null) {
                number = Integer.parseInt(numberStr) + 1;
                paddingLength = numberStr.length(); // preserve existing padding
            }

            String newNumber = String.format("%0" + paddingLength + "d", number);
            return prefix + newNumber;
        }

        throw new IllegalArgumentException("Invalid payroll ID format: " + lastPayrollId);
    }


    public String generateNextPayrollId(String prefix, String lastUsedPayrollId) {
        int nextNumber = 1;

        if (lastUsedPayrollId != null && lastUsedPayrollId.startsWith(prefix)) {
            String numberPart = lastUsedPayrollId.substring(prefix.length());
            try {
                nextNumber = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                // fallback to 1 if number part is invalid
                nextNumber = 1;
            }
        }

        // Format to 2-digit number with leading zeros: e.g., 01, 02, ... 10
        return String.format("%s%02d", prefix, nextNumber);
    }








}


