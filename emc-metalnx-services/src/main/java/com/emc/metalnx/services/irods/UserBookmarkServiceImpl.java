 /* Copyright (c) 2018, University of North Carolina at Chapel Hill */
 /* Copyright (c) 2015-2017, Dell EMC */
 


package com.emc.metalnx.services.irods;

import com.emc.metalnx.core.domain.dao.UserBookmarkDao;
import com.emc.metalnx.core.domain.dao.UserDao;
import com.emc.metalnx.core.domain.entity.DataGridUser;
import com.emc.metalnx.core.domain.entity.DataGridUserBookmark;
import com.emc.metalnx.services.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserBookmarkServiceImpl implements UserBookmarkService {

    @Autowired
    UserDao userDao;

    @Autowired
    UserBookmarkDao userBookmarkDao;

    @Autowired
    IRODSServices irodsServices;

    @Autowired
    AdminServices adminServices;

    @Autowired
    UserService userService;

    @Autowired
    CollectionService collectionService;

    private static final Logger logger = LoggerFactory.getLogger(UserBookmarkServiceImpl.class);

    @Override
    public boolean updateBookmarks(DataGridUser user, Set<String> toAdd, Set<String> toRemove) {

        boolean operationResult = true;

        try {
            if (toAdd != null) {
                for (String path : toAdd) {
                    if (!findBookmarksForUserAsString(user).contains(path)) {
                        userBookmarkDao.addByUserAndPath(user, path, collectionService.isCollection(path));
                    }
                }
            }

            if (toRemove != null) {
                for (String path : toRemove) {
                    userBookmarkDao.removeByUserAndPath(user, path);
                }
            }
        }
        catch (Exception e) {
            operationResult = false;
            logger.error("Could not modify user bookmark for {}", user.getUsername(), e);
        }

        return operationResult;
    }

    @Override
    public List<String> findBookmarksForUserAsString(DataGridUser user) {
        List<DataGridUserBookmark> bookmarks = userBookmarkDao.findByUser(user);
        List<String> strings = new ArrayList<String>();

        for (DataGridUserBookmark bookmark : bookmarks) {
            strings.add(bookmark.getPath());
        }

        return strings;
    }

    @Override
    public List<DataGridUserBookmark> findBookmarksOnPath(String path) {
        return userBookmarkDao.findBookmarksByPath(path);
    }

    @Override
    public boolean removeBookmarkBasedOnPath(String path) {
        return userBookmarkDao.removeByPath(path);
    }

    @Override
    public boolean removeBookmarkBasedOnRelativePath(String path) {
        return userBookmarkDao.removeByParentPath(path);
    }

    @Override
    public List<DataGridUserBookmark> findBookmarksPaginated(DataGridUser user, int start, int length, String searchString, String orderBy,
            String orderDir, boolean onlyCollections) {
        return userBookmarkDao.findByUserPaginated(user, start, length, searchString, orderBy, orderDir, onlyCollections);
    }

    @Override
    public List<DataGridUserBookmark> findBookmarksPaginated(DataGridUser user, int start, int length, String searchString, List<String> orderBy,
            List<String> orderDir, boolean onlyCollections) {
        return userBookmarkDao.findByUserPaginated(user, start, length, searchString, orderBy, orderDir, onlyCollections);
    }

    @Override
    public boolean removeBookmarkBasedOnUser(DataGridUser user) {
        return userBookmarkDao.removeByUser(user);
    }
    
    @Override
    public boolean updateBookmark(String oldPath, String newPath) {
    	return userBookmarkDao.updateBookmark(oldPath, newPath);
    }

}
