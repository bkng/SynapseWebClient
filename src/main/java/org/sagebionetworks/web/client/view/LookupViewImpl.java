package org.sagebionetworks.web.client.view;

import org.sagebionetworks.web.client.DisplayConstants;
import org.sagebionetworks.web.client.DisplayUtils;
import org.sagebionetworks.web.client.IconsImageBundle;
import org.sagebionetworks.web.client.SageImageBundle;
import org.sagebionetworks.web.client.widget.footer.Footer;
import org.sagebionetworks.web.client.widget.header.Header;
import org.sagebionetworks.web.client.widget.header.Header.MenuItems;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LookupViewImpl extends Composite implements LookupView {

	public interface LookupViewImplUiBinder extends UiBinder<Widget, LookupViewImpl> {}

	@UiField
	SimplePanel header;
	@UiField
	SimplePanel footer;
	@UiField
	SpanElement lookupId;
	@UiField
	SpanElement resultText;
	
	private Presenter presenter;
	private IconsImageBundle iconsImageBundle;
	private SageImageBundle sageImageBundle;
	private Header headerWidget;
	private Window lookingUpWindow;
	
	@Inject
	public LookupViewImpl(LookupViewImplUiBinder binder,
			Header headerWidget, Footer footerWidget, IconsImageBundle icons,
			SageImageBundle sageImageBundle) {		
		initWidget(binder.createAndBindUi(this));

		this.iconsImageBundle = icons;
		this.sageImageBundle = sageImageBundle;
		this.headerWidget = headerWidget;
		
		header.add(headerWidget.asWidget());
		footer.add(footerWidget.asWidget());
		headerWidget.setMenuItemActive(MenuItems.PROJECTS);

	}


	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;		
		headerWidget.refresh();				
		com.google.gwt.user.client.Window.scrollTo(0, 0); // scroll user to top of page
	}
	
	@Override
	public void showLookupFailed(String entityId) {
		if(lookingUpWindow != null) {
			lookingUpWindow.hide();
		}
		resultText.setInnerHTML(DisplayConstants.LABEL_ENTITY_NOT_FOUND);
	}


	@Override
	public void showLooking(String entityId) {
		lookupId.setInnerHTML(entityId);
		
		if(lookingUpWindow == null) {
			createLookingUpWindow();
		}
		setWindowMessage(DisplayConstants.LABEL_SEARCHING);
		lookingUpWindow.show();
	}

	@Override
	public void doneLooking() {
		lookingUpWindow.hide();
	}

	@Override
	public void showForwarding() {
		if(lookingUpWindow == null) {
			createLookingUpWindow();
		}
		setWindowMessage(DisplayConstants.LABEL_FOUND_FORWARDING);
		lookingUpWindow.show();
	}


	@Override
	public void showUnknownType(String type, String id) {
		lookingUpWindow.hide();
		resultText.setInnerHTML(DisplayConstants.LABEL_ENTITY_NOT_FOUND);
	}

	@Override
	public void showErrorMessage(String message) {
		DisplayUtils.showErrorMessage(message);
	}

	@Override
	public void showLoading() {
	}


	@Override
	public void showInfo(String title, String message) {
		DisplayUtils.showInfo(title, message);
	}


	@Override
	public void clear() {
		if(lookingUpWindow != null) {
			lookingUpWindow.hide();
		}
		resultText.setInnerHTML("");		
	}
	
	/*
	 * Private methods
	 */
	private void createLookingUpWindow() {
		lookingUpWindow = new Window();
		lookingUpWindow.setModal(true);		
		lookingUpWindow.setHeight(114);
		lookingUpWindow.setWidth(221);		
		lookingUpWindow.setBorders(false);
		lookingUpWindow.setBodyStyleName("whiteBackground");				
	}
	
	private void setWindowMessage(String message) {
		lookingUpWindow.removeAll();
		SafeHtmlBuilder shb = new SafeHtmlBuilder();
		shb.appendHtmlConstant(DisplayUtils.getIconHtml(sageImageBundle.loading31()) + " ")
		.appendEscaped(message);
		lookingUpWindow.add(new HTML(shb.toSafeHtml()), new MarginData(20, 0, 0, 45));
	}



}
