package org.hbrs.se2.project.aldavia.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.aldavia.control.BewerbungsOverviewStudent;
import org.hbrs.se2.project.aldavia.dtos.BewerbungsDTO;
import org.hbrs.se2.project.aldavia.dtos.TaetigkeitsfeldDTO;
import org.hbrs.se2.project.aldavia.dtos.UserDTO;
import org.hbrs.se2.project.aldavia.util.Globals;

import java.util.List;

@CssImport("./styles/views/bewerbungsOverview/bewerbungsOverviewStudent.css")
@Route(value = "bewerbungsOverviewStudent")
public class BewerbungsOverviewStudentView extends Div {

    private final BewerbungsOverviewStudent bewerbungsOverviewStudent;
    private final UserDTO currentUser;

    public BewerbungsOverviewStudentView(BewerbungsOverviewStudent bewerbungsOverviewStudent) {
        this.bewerbungsOverviewStudent = bewerbungsOverviewStudent;
        currentUser = getCurrentUser();
        setUpUI();
    }

    private Icon createRejected(){
        Icon rejected = new Icon(VaadinIcon.CLOSE);
        rejected.addClassName("rejected");
        return rejected;
    }

    private Icon createAccepted(){
        Icon accepted = new Icon(VaadinIcon.CHECK);
        accepted.addClassName("accepted");
        return accepted;
    }

    private Icon createPending(){
        Icon pending = new Icon(VaadinIcon.CLOCK);
        pending.addClassName("pending");
        return pending;
    }

    private void setUpUI() {
        add(new H1("Deine Bewerbungen"));
        add(createBewerbungenLayout());
    }

    private VerticalLayout createBewerbungenLayout(){
        try {
            List<BewerbungsDTO> bewerbungen = bewerbungsOverviewStudent.getBewerbungenStudent(currentUser.getUserid());
            if(bewerbungen.isEmpty()){
                return new VerticalLayout(new H3("Du hast dich noch auf keine Stellenanzeige beworben."));
            }
            VerticalLayout layout = new VerticalLayout();
            layout.setSizeFull();
            layout.setClassName("bewerbungen-layout");
            for (BewerbungsDTO bewerbung : bewerbungen) {
                layout.add(createBewerbung(bewerbung));
            }
            return layout;
        }
        catch (Exception e){
            Notification.show(e.toString());
        }
        return new VerticalLayout();
    }

    private Div createBewerbung(BewerbungsDTO bewerbung){
        Div div = new Div();
        div.addClassName("card");
        div.add(StellenanzeigeBewerbungsLayout(bewerbung));
        return div;
    }

    private HorizontalLayout StellenanzeigeBewerbungsLayout(BewerbungsDTO bewerbung){
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("stellenanzeige-bewerbungs-layout");
        layout.add(createStellenanzeigenInfosLayout(bewerbung));
        layout.add(createBewerbungsstatus(bewerbung));
        return layout;
    }

    private VerticalLayout createBewerbungsstatus(BewerbungsDTO bewerbungsDTO){
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("bewerbungsstatus-layout");
        layout.add(new Label("Beworben am: " + bewerbungsDTO.getDatum().toString()));
        switch (bewerbungsDTO.getStatus()){
           case "zusage":
                layout.add(createAccepted());
                break;
          case "absage":
                layout.add(createRejected());
                break;
          default:
                layout.add(createPending());
                break;
        }
        layout.add(removeBewerbung(bewerbungsDTO));
        return layout;
    }

    private Button removeBewerbung(BewerbungsDTO bewerbung){
        Button button = new Button("Bewerbung zurückziehen");
        button.addClassName("remove-bewerbung-button");
        button.addClickListener(e -> {
            try {
                bewerbungsOverviewStudent.removeBewerbung(bewerbung);
                UI.getCurrent().getPage().reload();
            }
            catch (Exception ex){
                Notification.show(ex.toString());
            }
        });
        return button;
    }

    private VerticalLayout createStellenanzeigenInfosLayout(BewerbungsDTO bewerbung){
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("stellenanzeigen-infos-layout");
        Anchor anchor = new Anchor();
        anchor.setText(bewerbung.getStellenanzeige().getUnternehmen().getName());
        anchor.setHref("unternehmen/" + bewerbung.getStellenanzeige().getUnternehmen().getName());
        layout.add(anchor);
        layout.add(new H2(bewerbung.getStellenanzeige().getBezeichnung()));
        layout.add(new H3(bewerbung.getStellenanzeige().getBeschaeftigungsverhaeltnis()));
        layout.add(createVonBis(bewerbung.getStellenanzeige().getStart().toString(), bewerbung.getStellenanzeige().getEnde().toString()));
        layout.add(createTaetigkeitenLayout(bewerbung));
        return layout;
    }

    private HorizontalLayout createVonBis(String von, String bis){
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("von-bis-layout");
        layout.add(new Label("Von " + von));
        layout.add(new Label("Bis " + bis));
        return layout;
    }

    private HorizontalLayout createTaetigkeitenLayout(BewerbungsDTO bewerbung){
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("taetigkeiten-layout");
        layout.add(new Label("Tätigkeiten: "));
        for (TaetigkeitsfeldDTO taetigkeit : bewerbung.getStellenanzeige().getTaetigkeitsfelder()) {
            Div div = new Div();
            div.addClassName("taetigkeit");
            div.setText(taetigkeit.getName());
            layout.add(div);
        }
        return layout;
    }



    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }
}
