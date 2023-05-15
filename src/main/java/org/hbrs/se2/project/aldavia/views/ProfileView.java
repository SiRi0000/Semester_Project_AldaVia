package org.hbrs.se2.project.aldavia.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.aldavia.control.StudentProfileControl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Route(value = "profile")
@PageTitle("Profil")
public class ProfileView extends Div implements HasUrlParameter<String> {

    @Autowired
    private StudentProfileControl studentProfileControl;
    private H1 title = new H1("Profil");
    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location
                .getQueryParameters();

        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        addTextToView(parametersMap.get("username").get(0));
    }
    public ProfileView(StudentProfileControl studentProfileControl) {
        addClassName("profile-view");
        add(title);
    }

    public void addTextToView(String text) {
        try{
        add(new Text(studentProfileControl.getStudentProfile(text).toString()));
        }catch (Exception e){
            add(new Text("Fehler beim Laden des Profils"));
        }
    }


    // TEST TEST

}
