package com.femiproject.librarysystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MemberManager {
    private List<Member> members;
    private final String member_file = "members.json";
    private final ObjectMapper objectMapper;

    public MemberManager() {
        this.members = new ArrayList<>();
        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
        loadMembers();
    }

    private void loadMembers() {
        try {
            File file = new File(member_file);
            if (file.exists() && file.length() > 0) {
                List<Member> loadedMembers = objectMapper.readValue(file, new TypeReference<List<Member>>() {
                });
                members = loadedMembers;
                System.out.println("Members loaded successfully from " + member_file);
            } else {
                System.out.println("No existing members file found. Starting with empty member list.");
            }
        } catch (IOException e) {
            System.err.println("Error loading members: " + e.getMessage());
            members = new ArrayList<>();
        }
    }

    public void saveMembers() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(member_file), members);
            System.out.println("Members saved successfully to " + member_file);
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }

    public Member getMemberById(String memberId) {
        return members.stream()
                .filter(m -> m.getMemberId().equals(memberId))
                .findFirst()
                .orElse(null);
    }

    public Member getMemberByName(String name) {
        return members.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void addMember(Member member) {
        if (member != null) {
            members.add(member);
            saveMembers();
            System.out.println("Member added: " + member.getName() + " (ID: " + member.getMemberId() + ")");
        }
    }

    public void removeMember(String memberId) {
        boolean removed = members.removeIf(m -> m.getMemberId().equals(memberId));
        if (removed) {
            saveMembers();
            System.out.println("Member removed successfully.");
        } else {
            System.out.println("Member not found.");
        }
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    public boolean memberExists(String memberId) {
        return getMemberById(memberId) != null;
    }
}