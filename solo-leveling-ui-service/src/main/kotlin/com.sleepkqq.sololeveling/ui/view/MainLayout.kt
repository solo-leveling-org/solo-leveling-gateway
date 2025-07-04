package com.sleepkqq.sololeveling.ui.view

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.Footer
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Header
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.SvgIcon
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.router.Layout
import com.vaadin.flow.server.auth.AnonymousAllowed
import com.vaadin.flow.server.menu.MenuConfiguration
import com.vaadin.flow.theme.lumo.LumoUtility

@Layout
@AnonymousAllowed
class MainLayout : AppLayout() {

	private val viewTitle = H1()

	init {
		primarySection = Section.DRAWER
		addDrawerContent()
		addHeaderContent()
	}

	private fun addHeaderContent() {
		val toggle = DrawerToggle()
		toggle.setAriaLabel("Menu toggle")

		viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE)

		addToNavbar(toggle, viewTitle)
	}

	private fun addDrawerContent() {
		val appName = Span("Solo leveling")
		appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE)
		val header = Header(appName)

		val scroller = Scroller(createNavigation())

		addToDrawer(header, scroller, createFooter())
	}

	private fun createNavigation(): SideNav {
		val nav = SideNav()

		MenuConfiguration.getMenuEntries()
			.forEach {
				it.icon?.let { icon ->
					nav.addItem(SideNavItem(it.title, it.path, SvgIcon(icon)))
				}
					?: nav.addItem(SideNavItem(it.title(), it.path()))
			}

		return nav
	}

	private fun createFooter(): Footer = Footer()

	override fun afterNavigation() {
		super.afterNavigation()
		viewTitle.text = this.currentPageTitle
	}

	private val currentPageTitle: String
		get() = MenuConfiguration.getPageHeader(content).orElse("")
}
