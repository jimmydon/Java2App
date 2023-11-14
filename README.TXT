
Appointment Application v1.2
Author: Don Logan

*********************************************************


Update: fixed delete of appointment to show appointment id and appointment type.
Update: fixed issue to show when an attempt to add an appointment overlaps with an existing appointment.
*********************************************************
IDE Information:
IntelliJ IDEA 2021.2 (Community Edition)
Build #IC-212.4746.92, built on July 27, 2021
Runtime version: 11.0.11+9-b1504.13 aarch64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
System: macOS Big Sur M1

Libraries:
Java JDK: openjdk-16 Azul Zulu Community version 16.00.01
JavaFX SDK: 11.0.2
MySQL Driver: mysql-connector-java-8.0.23

*********************************************************

Instructions:

If using Apple M1 the above SDK works, if using Intel Mac:

1. Go to Project Structure
2. select Project settings
3. project SDK and select -may have to install-  1.7 Azul Zulu Community version 15.0
*********************************************************

Application:

The Application purpose is to allow users to set up appointments. The appointments are set up for customers in
the system. It allows the user to add modify and delete customers and also add modify and delete appointments.

1. It alerts for any appointments scheduled to start in 15mins or less.
2. Will alert if appointments overlap, or end time is before start time.
3. Will alert if an appointment time is selected before the current time.
4. Keeps track of all user logins in a file login_activity.txt located in root folder



The system generates three reports:
    1. The total number of customer appointments by type and month -access by a button on GUI
    2. a schedule for each contact -access by selecting contact in dropdown then clicking button on GUI
    3. The total number of customer appointments by title and month. -access by a button on GUI

 The default user is: test and the password is also:test
*********************************************************

 Lambda:
  https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.8

 "In addition to the usual process of creating an interface instance by declaring and instantiating
 a class (§15.9), instances of functional interfaces can be
 created with method reference expressions and lambda expressions (§15.13, §15.27)."

The application makes use of numerous lambdas to implement a functional interface one example:

 Predicate<TextField> emptyValue = s -> s.getText().isEmpty();

I created a class TextFieldIsEmpty  then e.g. used as follows:

   TextFieldIsEmpty tstText = new TextFieldIsEmpty();
   if (tstText.emptyValue.test(loginView.txtUserName))

   it takes one input and returns a boolean. The use of lambdas allows for very concise code,
   that retains readability and also runs more efficiently.

 Another example of using a functional interface with a lambda:

 public static final Predicate<String> preLenAtLeast5 = t -> t.length() > 4;

 This allowed for composition in testing input values:

            boolean outcome;

             for (TextField textField : lstTextField) {

                 outcome = nonNullPredicate.and(TextFieldMin5.preLenAtLeast5).test(textField.getText());
                 if (!outcome) {
                     textField.requestFocus();
                     return textField.getId();
                 }
             }

 where nonNullPredicate is defined with class scope
              private final Predicate<String> nonNullPredicate = Objects::nonNull;
*********************************************************


 The JavaDocs are in the root directory in a folder called Documentation











