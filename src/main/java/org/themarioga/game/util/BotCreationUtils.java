package org.themarioga.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;

public class BotCreationUtils {

	private static final Logger logger = LoggerFactory.getLogger(BotCreationUtils.class);

	private BotCreationUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static void setWebhook(String webhookURL, String webhookCertPath, TelegramClient telegramClient) {
		try {
			SetWebhook.SetWebhookBuilder webhookBuilder = SetWebhook.builder().url(webhookURL);

			InputFile certificate = BotCreationUtils.getCertificate(webhookCertPath);
			if (certificate != null) {
				logger.info("Sending certificate {}...", webhookCertPath);

				webhookBuilder.certificate(certificate);
			}

			telegramClient.execute(webhookBuilder.build());
		} catch (TelegramApiException e) {
			logger.info("Error setting webhook");
		}
	}

	public static void deleteWebhook(TelegramClient telegramClient) {
		try {
			telegramClient.execute(new DeleteWebhook());
		} catch (TelegramApiException e) {
			logger.info("Error deleting webhook");
		}
	}

	public static InputFile getCertificate(String path) {
		InputFile certificate = null;
		File file = new File(path);
		if (file.exists() && file.canRead()) {
			certificate = new InputFile(file);
		}
		return certificate;
	}

}
