/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package drivethru;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DriveThruSpeechlet implements Speechlet {
	private AmazonDynamoDBClient amazonDynamoDBClient;
	
	private DriveThruManager driveThruManager;

	private SkillContext skillContext;

	@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		initializeComponents();

		// if user said a one shot command that triggered an intent event,
		// it will start a new session, and then we should avoid speaking too
		// many words.
		skillContext.setNeedsMoreHelp(false);
	}

	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		return driveThruManager.getLaunchResponse(request, session);
	}

	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		initializeComponents();

		Intent intent = request.getIntent();

		// After Alexa welcomes the user, we ask for the user name
		if ("UserNameIntent".equals(intent.getName())) {
			return driveThruManager.getUserNameIntentResponse(intent, session, skillContext);

		// When Alexa prompts the user for the order, he/she might ask for the categories
		} else if ("CategoryInquiryIntent".equals(intent.getName())) {
			return driveThruManager.getCategoryInquiryIntent(intent, session, skillContext);

		// When Alexa prompts the user for the order, he/she might ask for the menu items			
		} else if ("InquiryIntent".equals(intent.getName())) {
			return driveThruManager.getInquiryIntent(intent, session, skillContext);

		// When Alexa prompts the user for the order, he might place the order			
		} else if ("OrderPlacementIntent".equals(intent.getName())) {
			return driveThruManager.getOrderPlacementIntent(intent, session, skillContext);

//		} else if ("OrderChangeIntent".equals(intent.getName())) {
//			return driveThruManager.getOrderChangeIntent(intent, session, skillContext);

		// When the user asks the Alexa to repeat the order			
		} else if ("OrderRepeatIntent".equals(intent.getName())) {
			return driveThruManager.getOrderRepeatIntent(intent, session, skillContext);

		// When the user asks the Alexa to confirm the order			
		} else if ("OrderConfirmationIntent".equals(intent.getName())) {
			return driveThruManager.getOrderConfirmationIntent(intent, session, skillContext);
			
		} else if ("AMAZON.HelpIntent".equals(intent.getName())) {
			return driveThruManager.getHelpIntentResponse(intent, session, skillContext);

		} else if ("AMAZON.CancelIntent".equals(intent.getName())) {
			return driveThruManager.getExitIntentResponse(intent, session, skillContext);

		} else if ("AMAZON.StopIntent".equals(intent.getName())) {
			return driveThruManager.getExitIntentResponse(intent, session, skillContext);

		} else {
			throw new IllegalArgumentException("Unrecognized intent: " + intent.getName());
		}
	}

	@Override
	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Initializes the instance components if needed.
	 */
	private void initializeComponents() {
		if (amazonDynamoDBClient == null) {
			amazonDynamoDBClient = new AmazonDynamoDBClient();
			driveThruManager = new DriveThruManager(amazonDynamoDBClient);
			skillContext = new SkillContext();
		}
	}
}
