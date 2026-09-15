package dmit2015.view;

import dmit2015.model.StudentInfo;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class StudentFormBean implements Serializable {

    private int submissionCount;    // getter

    @Inject
    private StudentListSession studentListSession;

    private StudentInfo studentInfo = new StudentInfo(); // getter

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void removeStudent(StudentInfo existingStudentInfo) {
        studentListSession.remove(existingStudentInfo);
    }

    public void submit() {
        submissionCount++;
        studentListSession.add(studentInfo);

        FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Form Submitted",
                String.format("Welcome %s to %s program (%s)",
                        studentInfo.getFullName(),
                        studentInfo.getProgram(),
                        studentInfo.isFullTime() ? "Full Time" : "Part Time"
                        )

        );
        FacesContext.getCurrentInstance()
                .addMessage(null, message);
        studentInfo = new StudentInfo();
    }

    public int getSubmissionCount() {
        return submissionCount;
    }


}
