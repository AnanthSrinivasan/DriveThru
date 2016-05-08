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

        speechText = "Welcome..." + newDriveThruUserName 
        		+ " How can I help you today ?";
	    repromptText = newDriveThruUserName + " Can you please give me your order ?";

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
        // Speak welcome message and ask user questions
        String speechText, repromptText;
        List<DriveThruCategoryDataItem> driveThruCategoryDataItems = 
        		driveThruDao.getCategories();

	    speechText = "Here are the categories... ";
	    for(DriveThruCategoryDataItem driveThruCategoryDataItem : driveThruCategoryDataItems) {
	        speechText += driveThruCategoryDataItem.getCategoryName() + " , ";
	    }
	    repromptText = "Can you please give me your order ?";

        return getAskSpeechletResponse(speechText, repromptText);
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
