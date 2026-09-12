package dmit2015.view;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class StudentFormBean implements Serializable {

    private int submissionCount;    // getter

    private String fullName;    // getter/setter
    private String program;     // getter/setter
    private boolean fullTime;   // getter/setter

    public void submit() {
        submissionCount++;

        FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Form Submitted",
                String.format("Welcome %s to %s program (%s)",
                        fullName,
                        program,
                        fullTime ? "Full Time" : "Part Time"
                        )

        );
        FacesContext.getCurrentInstance()
                .addMessage(null, message);
        fullName = null;
        program = null;
        fullTime = true;
    }

    public int getSubmissionCount() {
        return submissionCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFullTime(boolean fullTime) {
        this.fullTime = fullTime;
    }
}
