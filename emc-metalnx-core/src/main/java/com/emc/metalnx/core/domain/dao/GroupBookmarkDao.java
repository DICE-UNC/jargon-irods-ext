 /* Copyright (c) 2018, University of North Carolina at Chapel Hill */
 /* Copyright (c) 2015-2017, Dell EMC */
 

package com.emc.metalnx.core.domain.dao;

import com.emc.metalnx.core.domain.dao.generic.GenericDao;
import com.emc.metalnx.core.domain.entity.DataGridGroup;
import com.emc.metalnx.core.domain.entity.DataGridGroupBookmark;

import java.util.List;

public interface GroupBookmarkDao extends GenericDao<DataGridGroupBookmark, Long> {

    /**
     * Add a bookmark to a group using the path and group entity
     *
     * @param group
     * @param path
     * @param isCollection
     * @return a confirmation that the insertion has been successfully
     */
    public Long addByGroupAndPath(DataGridGroup group, String path, boolean isCollection);

    /**
     * Removes a bookmark based on the path and the group
     *
     * @param group
     * @param path
     * @return a confirmation that the deletion has been successfully
     */
    public boolean removeByGroupAndPath(DataGridGroup group, String path);

    /**
     * Removes a bookmark based on the group
     *
     * @param group
     * @return a confirmation that the deletion has been successfully
     */
    public boolean removeByGroup(DataGridGroup group);

    /**
     * Retrieve a given GroupBookmark based on the group
     *
     * @param group
     * @return a {@link DataGridGroupBookmark}
     */
    public List<DataGridGroupBookmark> findByGroup(DataGridGroup group);

    /**
     * Retrieves all the bookmarks on a given path
     *
     * @param path
     * @return list of {@link DataGridGroupBookmark}
     */
    public List<DataGridGroupBookmark> findBookmarksByPath(String path);

    /**
     * Removes a bookmark based on the given path
     *
     * @param path
     *            path to remove any bookmark
     * @return a confirmation that the deletion has been successful
     */
    public boolean removeByPath(String path);

    /**
     * Removes all existing bookmarks whose parent path is the given path. Basically, if the
     * following bookmarks exist:
     * a/b/c
     * a/b/c/d
     * x/y/z/a/b/c
     * and the directory "a" gets deleted. Both "a/b/c" and "a/b/c/d" should be removed from
     * bookmarks since they no longer exist. But "x/y/z/a/b/c" should be kept.
     *
     * @param parentPath
     *            path to remove any bookmark
     * @return a confirmation that the deletion has been successful
     */
    public boolean removeByParentPath(String parentPath);

    /**
     * Find group bookmarks with limits and filter for pagination
     *
     * @param groupIds
     * @param offset
     * @param limit
     * @param searchString
     * @param orderBy
     * @param orderDir
     * @param onlyCollections
     * @return
     */
    public List<DataGridGroupBookmark> findGroupBookmarksByGroupsIds(String[] groupIds, int offset, int limit, String searchString, String orderBy,
            String orderDir, boolean onlyCollections);

    /**
     * Gives the total number of group bookmarks for a list of groups
     *
     * @param groupIds
     * @return
     */
    public Long countGroupBookmarksByGroupsIds(String[] groupIds);

    /**
     * Changes an existing bookmark to a new value.
     * 
     * @param oldPath
     * 			existing path that will be updated
     * @param newPath
     * 			new path
     * @return True, if oldePath was successfully changed to newPath. False, otherwise.
     */
	boolean updateBookmark(String oldPath, String newPath);
}
