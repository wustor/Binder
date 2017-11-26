// RemoteService.aidl
package com.wustor.aidl;

// Declare any non-default types here with import statements
import com.wustor.aidl.People;
interface PeopleManager {
   List<People> getPeople();
  void addPeople(in People people);
}
