package mypackage.model;

import java.util.ArrayList;

public class Group {
    private int id;
    private String name;
    private String BossName;
    private String BossFirstName;
    private ArrayList<Integer> members = new ArrayList<>();

    public Group() {
        this.id = 0;
        this.name = "";
    }

    public Group(int id, String name, String bossName, String bossFirstName) {
        this.id = id;
        this.name = name;
        this.BossName = bossName;
        this.BossFirstName = bossFirstName;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getBossName() {
        return BossName;
    }
    public String getBossFirstName() {
        return BossFirstName;
    }
    public ArrayList<Integer> getMembers() {
        return members;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBossName(String bossName) {
        this.BossName = bossName;
    }
    public void setBossFirstName(String bossFirstName) {
        this.BossFirstName = bossFirstName;
    }
    public void setMembers(ArrayList<Integer> members) {
        this.members = members;
    }
    public void addMember(int memberId) {
        if (!members.contains(memberId)) {
            members.add(memberId);
        }
    }
    public void removeMember(int memberId) {
        members.remove(Integer.valueOf(memberId));
    }
}
