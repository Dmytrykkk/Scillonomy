object Main{
 var  office = new Office(null,null)
 val platform = new Platform(100000, 1)
 var simulation = new Simulation
 var baseAddress = new Address("Country", "City")

 var coach1 = new Coach("First Coach", 30, baseAddress, 12, 1, 5)

 var student1 = new Student("First Student", 18, baseAddress)

 coach1.addStudent(student1)

 for (i <- 1 to 12) {
  simulation.newMonth()

  var student = new Student("Student Number: " + i, 18, baseAddress)
  coach1.addStudent(student)
 }
}