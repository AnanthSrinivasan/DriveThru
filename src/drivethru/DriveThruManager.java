/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package drivethru;

import java.util.List;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import drivethru.storage.DriveThruCategoryDataItem;
import drivethru.storage.DriveThruDynamoDbClient;
import drivethru.storage.DriveThruOrderDataItem;
import drivethru.storage.DriveThruSessionDataItem;
import drivethru.storage.IDriveThruDao;

/**
 * The {@link DriveThruManager} receives various events and intents and manages the flow of the
 * application.
 */
public class DriveThruManager {

    /**
     * Intent slot for user name.
     */
    private static final String SLOT_USER_NAME = "UserName";

    /**
     * Intent slot for menu items.
     */    
    private static final String SLOT_MENU_ITEMS = "MenuItems";

    /**
     * Intent slot for ingredients.
     */
    private static final String SLOT_INGREDIENTS = "Ingredients";
    
    private static final String QUANTITY = "Count";
    
    private final IDriveThruDao driveThruDao;

    public DriveThruManager(final AmazonDynamoDBClient amazonDynamoDbClient) {
        driveThruDao = new DriveThruDynamoDbClient(amazonDynamoDbClient);
    }
    
    /**
     * Creates and returns response for Launch request.
     *
     * @param request
     *            {@link LaunchRequest} for this request
     * @param session
     *            Speechlet {@link Session} for this request
     * @return response for launch request
     */
    public SpeechletResponse getLaunchResponse(LaunchRequest request, Session session) {
        // Speak welcome message and ask user questions
        String speechText, repromptText;

        speechText = "Hello, Welcome to Sata Drive, "
        		+ "Whom do I have the pleasure of working with today ?";
        repromptText = "May I know your name ? ";

        return getAskSpeechletResponse(speechText, repromptText);
    }

    /*
     * In this intent, we get the name of the user and store it into the 
     * session table with the sessionId.
     * Here we prompt the user for placing the order.
     */
    public SpeechletResponse getUserNameIntentResponse(Intent intent, Session session, SkillContext skillContext) {
        // Speak welcome message and ask user questions
        String speechText, repromptText;

        String newDriveThruUserName =
        		DriveThruTextUtil.getUserName(intent.getSlot(SLOT_USER_NAME).getValue());

        speechText = "Welcome " + newDriveThruUserName 
        		+ ", How can I help you today ?";
	    repromptText = newDriveThruUserName + ", Can you please give me your order ?";

	    DriveThruSessionDataItem driveThruSessionDataItem = new DriveThruSessionDataItem();
	    driveThruSessionDataItem.setName(newDriveThruUserName);
	    driveThruSessionDataItem.setSessionId(session.getSessionId());
	    driveThruDao.storeSessionInformation(driveThruSessionDataItem);
	    
        return getAskSpeechletResponse(speechText, repromptText);
    }   

    public SpeechletResponse getInquiryIntent(Intent intent, Session session, SkillContext skillContext) {
        // Speak welcome message and ask user questions
        String speechText, repromptText;

        speechText = "";
        repromptText = "";

        return getAskSpeechletResponse(speechText, repromptText);
    }   
    
    public SpeechletResponse getCategoryInquiryIntent(Intent intent, Session session, SkillContext skillContext) {
        String speechText, repromptText;
        List<DriveThruCategoryDataItem> driveThruCategoryDataItems = 
        		driveThruDao.getCategories();

	    speechText = "Here are the categories... ";
	    for(DriveThruCategoryDataItem driveThruCategoryDataItem : driveThruCategoryDataItems) {
	        speechText += driveThruCategoryDataItem.getCategoryName() + ", ";
	    }
	    repromptText = "Can you please give me your order ?";

        return getAskSpeechletResponse(speechText, repromptText);
    }

	public SpeechletResponse getOrderPlacementIntent(Intent intent, Session session, SkillContext skillContext) {
        String speechText, repromptText;
        String userName = driveThruDao.getSessionInformation(session.getSessionId()).getName();
        String menuItems = intent.getSlot(SLOT_MENU_ITEMS).getValue();
        String ingredients = intent.getSlot(SLOT_INGREDIENTS).getValue();        
        int count = 0;

        try {
        		count = Integer.parseInt(intent.getSlot(QUANTITY).getValue());
        } catch (NumberFormatException e) {
            speechText = "Sorry, I did not hear the quantity. Please say again?";
            return getAskSpeechletResponse(speechText, speechText);
        }        		
        		
        speechText = "Sure " + userName 
        		+ ", I am taking your Order.." 
        		+ " You ordered " +  count + menuItems + " with " + ingredients
        		+ " Whatelse can I add to your order ?";
	    repromptText = userName + ", Whatelse can I add to your order ?";

	    DriveThruOrderDataItem driveThruOrderDataItem = new DriveThruOrderDataItem();
	    driveThruOrderDataItem.setItemName(menuItems);
	    driveThruOrderDataItem.setOrderId(session.getSessionId() + "_" + userName + "_" + System.currentTimeMillis());
	    driveThruOrderDataItem.setPrice(2);
	    driveThruOrderDataItem.setQuantity(count);
	    driveThruDao.storeOrder(driveThruOrderDataItem);
	    
        return getAskSpeechletResponse(speechText, repromptText);
	}

	public SpeechletResponse getOrderChangeIntent(Intent intent, Session session, SkillContext skillContext) {
		// TODO Auto-generated method stub
		return null;
	}

	public SpeechletResponse getOrderRepeatIntent(Intent intent, Session session, SkillContext skillContext) {
        String speechText, repromptText;
        String userName = driveThruDao.getSessionInformation(session.getSessionId()).getName();
        List<DriveThruOrderDataItem> orders = driveThruDao.getOrdersBySessionId(session.getSessionId());
        
        String existingOrder = null;
        for(DriveThruOrderDataItem order : orders) {
        		existingOrder += order.getQuantity() + order.getItemName();
        }
        	
        speechText = "Sure " + userName 
        		+ ", Here is what I have on your Order.." + existingOrder 
        		+ " Do you want to add anything else to your order..."; 
	    repromptText = userName + ", Can I confirm to place the order ?";

        return getAskSpeechletResponse(speechText, repromptText);
	}

	public SpeechletResponse getOrderConfirmationIntent(Intent intent, Session session, SkillContext skillContext) {
        String speechText;
        double price = 0;
        String userName = driveThruDao.getSessionInformation(session.getSessionId()).getName();
        List<DriveThruOrderDataItem> orders = driveThruDao.getOrdersBySessionId(session.getSessionId());        

        for(DriveThruOrderDataItem order : orders) {
        		price += order.getPrice();
        }
        	
        speechText = "Sure " + userName 
        		+ ", Your order has been placed. And your total for today is " + price; 

        return getAskSpeechletResponse(speechText, speechText);
	}
    
    /**
     * Creates and returns response for the help intent.
     *
     * @param intent
     *            {@link Intent} for this request
     * @param session
     *            {@link Session} for this request
     * @param skillContext
     *            {@link SkillContext} for this request
     * @return response for the help intent
     */
    public SpeechletResponse getHelpIntentResponse(Intent intent, Session session,
            SkillContext skillContext) {
        return skillContext.needsMoreHelp() ? getAskSpeechletResponse(
                DriveThruTextUtil.COMPLETE_HELP + " So, how can I help?",
                DriveThruTextUtil.NEXT_HELP)
                : getTellSpeechletResponse(DriveThruTextUtil.COMPLETE_HELP);
    }

    /**
     * Creates and returns response for the exit intent.
     *
     * @param intent
     *            {@link Intent} for this request
     * @param session
     *            {@link Session} for this request
     * @param skillContext
     *            {@link SkillContext} for this request
     * @return response for the exit intent
     */
    public SpeechletResponse getExitIntentResponse(Intent intent, Session session,
            SkillContext skillContext) {
        return skillContext.needsMoreHelp() ? getTellSpeechletResponse("Okay. Whenever you're "
                + "ready, you can start placing your order.")
                : getTellSpeechletResponse("");
    }    

    /**
     * Returns an ask Speechlet response for a speech and reprompt text.
     *
     * @param speechText
     *            Text for speech output
     * @param repromptText
     *            Text for reprompt output
     * @return ask Speechlet response for a speech and reprompt text
     */
    private SpeechletResponse getAskSpeechletResponse(String speechText, String repromptText) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
    
    /**
     * Returns a tell Speechlet response for a speech and reprompt text.
     *
     * @param speechText
     *            Text for speech output
     * @return a tell Speechlet response for a speech and reprompt text
     */
    private SpeechletResponse getTellSpeechletResponse(String speechText) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

}
