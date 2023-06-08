package org.hbrs.se2.project.aldavia.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.RouterLink;
import org.hbrs.se2.project.aldavia.control.AuthorizationControl;
import org.hbrs.se2.project.aldavia.control.exception.ProfileException;
import org.hbrs.se2.project.aldavia.dtos.UserDTO;
import org.hbrs.se2.project.aldavia.util.Globals;


@CssImport("./styles/views/navbar/navbar.css")
public class LoggedInStateLayout extends AppLayout {

    private static AuthorizationControl authorizationControl;


    public LoggedInStateLayout() throws ProfileException {
        setUpUI();
    }

    public void setUpUI() throws ProfileException {
        addToNavbar(true, createHeaderContent());
    }


    private Component createHeaderContent() {



        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setId("header-neutral");
        Image logo = new Image("images/aldavia.png", "AldaVia logo");

        if(checkIfUserIsLoggedIn()){
            navigateStudentHomeLogo(logo);
        } else {
            navigateCompanyHomeLogo(logo);
        }

        layout.add(logo);

        HorizontalLayout topRightLayout = new HorizontalLayout();
        topRightLayout.setId("top-right-layout");
        topRightLayout.setWidthFull();
        topRightLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        topRightLayout.setAlignItems(FlexComponent.Alignment.CENTER);


        if(checkIfUserIsLoggedIn()){

            topRightLayout.add(new Label("Hallo, " + getCurrentUserName()));

            if(checkIfUserIsStudent()){

                topRightLayout.add(createMenuItemsStudent());
            } else if (checkIfUserIsUnternehmen()){
                topRightLayout.add(createMenuItemsUnternehmen());
            }
            // Logout-Button am rechts-oberen Rand.
            topRightLayout.add(createLogOutButton());
        }

        layout.add(topRightLayout);


        return layout;
    }


    private Component[] createMenuItemsStudent() {

        Tab[] tabs = new Tab[]{
                createTabProfileTab(),
        };
        tabs[0].setId("student-srofil-tab");
        return tabs;
    }

    private Component[] createMenuItemsUnternehmen() {

        Tab[] tabs = new Tab[]{
                createTabProfileTab(),
                createButtonCreateStellenanzeige()
        };
        tabs[0].setId("unternehmen-srofil-tab");
        return tabs;
    }


    private static Tab createTabProfileTab() {
        final Tab tab = new Tab();

        // Erstelle das Icon
        Icon iconUser = VaadinIcon.USER.create();
        iconUser.setSize("24px");
        iconUser.setColor("blue");

        // Erstelle das Text-Label
        Label label = new Label("Profile");

        // Erstelle den RouterLink mit dem Icon und dem Label als Inhalt

        RouterLink routerLink = null;

        if(checkIfUserIsStudent()) {
            routerLink = new RouterLink(null, StudentProfileView.class, getCurrentUserName());
            routerLink.add(iconUser, label);
        }

        //TODO Routerlink für Unternehmen
        /*else {
            routerLink = new RouterLink(null,CompanyProfileView.class, getCurrentUserName());
            routerLink.add(iconUser, label);
        }*/


        // Füge den RouterLink zum Tab hinzu
        tab.add(routerLink);
        return tab;
    }


    private static Tab createButtonCreateStellenanzeige() {
        final Tab tab = new Tab();
        Button button = new Button("Erstellen");
        button.addClickListener(event -> button.getUI().ifPresent(ui -> ui.navigate(CreateStellenanzeigeView.class)));
        tab.add(button);
        return tab;
    }

    private MenuBar createLogOutButton() {
        MenuBar bar = new MenuBar();
        Icon iconSignOut = VaadinIcon.SIGN_OUT.create();
        Text textLogOut = new Text("Log out");

        // Erstellen Sie ein neues Layout und legen Sie den Abstand zwischen den Elementen fest
        HorizontalLayout layout = new HorizontalLayout(iconSignOut, textLogOut);
        layout.setSpacing(true);

        MenuItem item = bar.addItem(layout, e -> logoutUser());
        item.setId("logout-button");

        // Setzen Sie den Hintergrund der MenuBar auf transparent
        bar.getStyle().set("background", "transparent");

        return bar;
    }


    private static void navigateStudentHomeLogo(Image img) {
        img.addClickListener(event -> img.getUI().ifPresent(ui -> ui.navigate(Globals.Pages.STUDENT_MAIN)));
    }

    private static void navigateCompanyHomeLogo(Image img) {
        img.addClickListener(event -> img.getUI().ifPresent(ui -> ui.navigate(Globals.Pages.COMPANY_MAIN)));
    }


    private static UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    private static boolean checkIfUserIsLoggedIn() {
        // Falls der Benutzer nicht eingeloggt ist, dann wird er auf die Startseite gelenkt
        UserDTO userDTO = getCurrentUser();
        if (userDTO == null) {
            UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
            return false;
        }
        return true;
    }

    private static boolean checkIfUserIsStudent(){
        if(!checkIfUserIsLoggedIn()){
            return false;
        }
        authorizationControl = new AuthorizationControl();
        return authorizationControl.isUserInRole(getCurrentUser(), Globals.Roles.STUDENT);
    }

    private static boolean checkIfUserIsUnternehmen(){
        if(!checkIfUserIsLoggedIn()){
            return false;
        }
        authorizationControl = new AuthorizationControl();
        return authorizationControl.isUserInRole(getCurrentUser(), Globals.Roles.UNTERNEHMEN);
    }

    private void logoutUser() {
        UI ui = this.getUI().get();
        ui.getSession().close();
        ui.getPage().setLocation("/");
    }

    //TODO:Methode spezifisch für Student und unternehmen

    public static String getCurrentUserName() {
        return getCurrentUser().getUserid();
    }

    /*private String getCurrentStudentName() throws ProfileException {
        Student student = studentControl.getStudent(getCurrentUserName());
        return student.getVorname();
    }*/

    private String createProfileURL(String username) {
        return Globals.Pages.PROFILE_VIEW + "?username=" + username;
    }






}
