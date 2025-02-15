package com.sleepkqq.sololeveling.view.component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import java.util.List;

@Tag("swiped-container")
@JsModule("./imports/swiped.js")
@CssImport(value = "./styles/swiped.css", themeFor = "swiped-container")
public class Swiped extends Div {

  public Swiped(List<String> items) {
    setClassName("swiped-container");

    items.forEach(item -> {
      var listItem = new Div();
      listItem.setText(item);
      listItem.setClassName("swiped-item");
      add(listItem);
    });

    getElement().executeJs(
        "Swiped.init({ query: '.swiped-item', list: true, left: 200, right: 200 });"
    );
  }
}
