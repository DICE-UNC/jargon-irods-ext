package com.emc.metalnx.services.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datacommons.api.NotificationsApi;
import org.datacommons.client.ApiException;
import org.datacommons.model.Notification;
import org.irods.jargon.core.exception.JargonRuntimeException;
import org.irods.jargon.extensions.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

	// @Autowired
	NotificationsApi apiInstance = new NotificationsApi();

	@Override
	public List<Notification> getAllNotification(String userId) {
		// TODO Auto-generated method stub
		List<Notification> result = new ArrayList<>();
		try {
			result = apiInstance.getNotification(userId);
		} catch (ApiException e) {
			logger.error("unable to retrieve notifications", e);
			throw new JargonRuntimeException("error retrieving notifications", e); // TODO: consider what exception
																					// hierarchy needs to look like
		}
		return result;
	}

	@Override
	public List<Notification> getNotificationById(String userId, String notificationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> getUnseenCounts(String userId) {
		Map<String, Integer> result = new HashMap<>();
		try {
			result = apiInstance.getUnseenCount(userId);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public void deleteNotifications(List<String> uuids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void markToSeen(List<String> uuids) {
		/*
		 * NOTE - Conversion should be kept in the services-- this should be the list of
		 * String/UUIDs. and the services should convert to any supported or desired
		 * format.
		 */
		/*
		 * try { apiInstance.markSeen(uuids); } catch (ApiException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
	}
}
