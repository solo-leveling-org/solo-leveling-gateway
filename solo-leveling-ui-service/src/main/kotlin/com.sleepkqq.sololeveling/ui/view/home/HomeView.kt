package com.sleepkqq.sololeveling.ui.view.home

import com.sleepkqq.sololeveling.ui.model.UserData
import com.sleepkqq.sololeveling.ui.service.TgAuthService
import com.vaadin.flow.component.Composite
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.dependency.Uses
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import jakarta.annotation.security.PermitAll
import org.vaadin.lineawesome.LineAwesomeIconUrl

@PageTitle("Home")
@Route("")
@Menu(order = 0.0, icon = LineAwesomeIconUrl.CAMPGROUND_SOLID)
@CssImport("./styles/home-view.css")
@Uses(Icon::class)
@PermitAll
class HomeView(tgAuthService: TgAuthService) : Composite<VerticalLayout>() {

	init {
		content.width = "100%"
		content.style.set("flex-grow", "1")
		content.alignItems = FlexComponent.Alignment.CENTER

		val currentUser = tgAuthService.currentUser
		val userCard = createUserCard(currentUser)
		content.add(userCard)

		val logoutButton = Button("Logout", VaadinIcon.SIGN_OUT.create())
		{ tgAuthService.logout() }
		logoutButton.addClassName("logout-button")
		content.add(logoutButton)
	}

	private fun createUserCard(user: UserData): VerticalLayout {
		val avatar = Image(user.photoUrl, "User Avatar")
		avatar.addClassName("user-avatar")

		val name = Span(user.firstName + " " + user.lastName)
		name.addClassName("user-name")

		val username = Paragraph("@" + user.getUsername())
		username.addClassName("user-username")

		val userInfo = VerticalLayout(name, username)
		userInfo.isSpacing = false
		userInfo.isPadding = false
		userInfo.addClassName("user-info")

		val cardContent = HorizontalLayout(avatar, userInfo)
		cardContent.alignItems = FlexComponent.Alignment.CENTER
		cardContent.isSpacing = true
		cardContent.addClassName("user-card-content")

		val userCard = VerticalLayout(cardContent)
		userCard.addClassName("user-card")
		userCard.isSpacing = false
		userCard.isPadding = true

		return userCard
	}
}
