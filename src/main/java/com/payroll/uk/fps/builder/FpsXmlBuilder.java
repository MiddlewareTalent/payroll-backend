/*
public class FpsXmlBuilder {

    public FullPaymentSubmission buildFpsSubmission(EmployerDetails employer, List<FpsEmployeeDTO> employees) {
        FullPaymentSubmission fps = new FullPaymentSubmission();

        fps.setEmployer(buildEmployer(employer));

        List<FullPaymentSubmission.Employee> fpsEmployees = new ArrayList<>();
        for (FpsEmployeeDTO dto : employees) {
            fpsEmployees.add(buildEmployee(dto));
        }
        fps.getEmployee().addAll(fpsEmployees);

        return fps;
    }

    private FullPaymentSubmission.Employer buildEmployer(EmployerDetails employerDetails) {
        FullPaymentSubmission.Employer employer = new FullPaymentSubmission.Employer();
        employer.setEmployerRef(employerDetails.getEmployerRef());
        employer.setOfficeNo(employerDetails.getTaxOffice().getOfficeNumber());
        employer.setPayeScheme(employerDetails.getPayeScheme());
        return employer;
    }

    private FullPaymentSubmission.Employee buildEmployee(FpsEmployeeDTO dto) {
        FullPaymentSubmission.Employee emp = new FullPaymentSubmission.Employee();

        emp.setNINO(dto.getNino());
        emp.setSurname(dto.getLastName());
        emp.setForename(dto.getFirstName());
        emp.setInitial(dto.getTitle());

        emp.setGender(dto.getGender());
        if (dto.getBirthDate() != null) {
            emp.setDateOfBirth(dto.getBirthDate().toString());
        }

        emp.setTaxCode(dto.getTaxCode());
        emp.setPayId(dto.getPayId());
        emp.setNiCategory(dto.getNiCategory());

        if (dto.getPaymentDate() != null) {
            emp.setPaymentDate(dto.getPaymentDate().toString());
        }

        emp.setGross(dto.getGrossPay());
        emp.setTax(dto.getTaxDeducted());
        emp.setNiEE(dto.getEmployeeNI());
        emp.setNiER(dto.getEmployerNI());

        // Year-To-Date
        emp.setYTDGross(dto.getYtdGrossPay());
        emp.setYTDTax(dto.getYtdTax());
        emp.setYTDNIEE(dto.getYtdEmployeeNI());
        emp.setYTDNIER(dto.getYtdEmployerNI());

        // Starter
        if (dto.isNewStarter()) {
            FullPaymentSubmission.Employee.Starter starter = new FullPaymentSubmission.Employee.Starter();
            starter.setStartDeclaration(dto.isOnStarterDeclaration() ? "A" : "C"); // Example: A, B, or C
            emp.setStarter(starter);
        }

        // Leaver
        if (dto.isLeaver() && dto.getLeavingDate() != null) {
            emp.setLeavingDate(dto.getLeavingDate().toString());
        }

        // Loans
        if (dto.isHasStudentLoan()) {
            emp.setStudentLoanInd("Y");
        }
        if (dto.isHasPostgraduateLoan()) {
            emp.setPostgradLoanInd("Y");
        }

        // Pension (receiving, not just eligible)
        if (dto.isReceivingOccupationalPension()) {
            emp.setOccPen("Y");
        }

        return emp;
    }
}
*/
