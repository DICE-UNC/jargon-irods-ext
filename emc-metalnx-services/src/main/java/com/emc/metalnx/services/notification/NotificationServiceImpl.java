package com.emc.metalnx.services.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.datacommons.api.NotificationsApi;
import org.datacommons.client.ApiException;
import org.datacommons.model.Notification;
import org.datacommons.model.UuidList;
import org.irods.jargon.core.exception.JargonRuntimeException;
import org.irods.jargon.extensions.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

	NotificationsApi apiInstance = new NotificationsApi();

	@Override
	public List<Notification> getAllNotification(String userId) {
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
	public Map<String, Integer> markToSeen(List<String> uuids) {
		/*
		 * NOTE - Conversion should be kept in the services-- this should be the list of
		 * String/UUIDs. and the services should convert to any supported or desired
		 * format.
		 */
		Map<String, Integer> result = new HashMap<>();
		try {
			UuidList uuidList = new UuidList();

			for (String uuid : uuids) {
				UUID temp = UUID.fromString(uuid);
				uuidList.addUuidsItem(temp);
			}
			result = apiInstance.markSeen(uuidList);
		} catch (

		ApiException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Integer> deleteNotifications(List<String> uuids) {
		/*
		 * NOTE - Conversion should be kept in the services-- this should be the list of
		 * String/UUIDs. and the services should convert to any supported or desired
		 * format.
		 */
		Map<String, Integer> result = new HashMap<>();
		try {
			UuidList uuidList = new UuidList();

			for (String uuid : uuids) {
				UUID temp = UUID.fromString(uuid);
				uuidList.addUuidsItem(temp);
			}
			result = apiInstance.deleteNotification(uuidList);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<Notification> getNotificationById(String userId, String notificationId) {
		// TODO Auto-generated method stub
		// To be used if needed
		return null;
	}
}
