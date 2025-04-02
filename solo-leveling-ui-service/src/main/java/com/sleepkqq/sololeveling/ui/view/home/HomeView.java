package com.sleepkqq.sololeveling.ui.view.home;

import com.sleepkqq.sololeveling.ui.model.UserData;
import com.sleepkqq.sololeveling.ui.service.TgAuthService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Home")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.CAMPGROUND_SOLID)
@CssImport("./styles/home-view.css")
@Uses(Icon.class)
@PermitAll
public class HomeView extends Composite<VerticalLayout> {

  public HomeView(TgAuthService tgAuthService) {
    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");
    getContent().setAlignItems(FlexComponent.Alignment.CENTER);

    var currentUser = tgAuthService.getCurrentUser();

    var userCard = createUserCard(currentUser);
    getContent().add(userCard);

    var logoutButton = new Button(
        "Logout",
        VaadinIcon.SIGN_OUT.create(),
        event -> tgAuthService.logout()
    );
    logoutButton.addClassName("logout-button");
    getContent().add(logoutButton);
  }

  private VerticalLayout createUserCard(UserData user) {
    var avatar = new Image(user.getPhotoUrl(), "User Avatar");
    avatar.addClassName("user-avatar");

    var name = new Span(user.getFirstName() + " " + user.getLastName());
    name.addClassName("user-name");

    var username = new Paragraph("@" + user.getUsername());
    username.addClassName("user-username");

    var userInfo = new VerticalLayout(name, username);
    userInfo.setSpacing(false);
    userInfo.setPadding(false);
    userInfo.addClassName("user-info");

    var cardContent = new HorizontalLayout(avatar, userInfo);
    cardContent.setAlignItems(FlexComponent.Alignment.CENTER);
    cardContent.setSpacing(true);
    cardContent.addClassName("user-card-content");

    var userCard = new VerticalLayout(cardContent);
    userCard.addClassName("user-card");
    userCard.setSpacing(false);
    userCard.setPadding(true);

    return userCard;
  }
}
