package cm.study.java.simulation;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class Context {

    private List<People2> people2List;

    private List<People> peopleList;

    public void setPeople2List(List<People2> people2List) {
        this.people2List = people2List;
    }

    public People2 selectPeople2() {
        int index = RandomUtils.nextInt(0, people2List.size());
        return people2List.get(index);
    }

    public void setPeopleList(List<People> peopleList) {
        this.peopleList = peopleList;
    }

    public People selectPeople() {
        int index = RandomUtils.nextInt(0, peopleList.size());
        return peopleList.get(index);
    }
}
