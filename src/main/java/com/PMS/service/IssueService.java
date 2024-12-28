package com.PMS.service;

import java.util.List;
import com.PMS.model.Issue;
import com.PMS.model.User;
import com.PMS.request.IssueRequest;

public interface IssueService {
    Issue getIssueById(Long issueId) throws Exception;

    List<Issue> getIssuesByProjectId(Long projectId) throws Exception;

    Issue createIssue(IssueRequest issueRequest, User user) throws Exception;

    void deleteIssue(Long issueId, Long userid) throws Exception;

    Issue addUserToIssue(Long issueId, Long userId) throws Exception;

    Issue updateStatus(Long issueId, String status) throws Exception;


}
