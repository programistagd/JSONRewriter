# JSON Rewriter
A functional utility library for quickly writing JSON converters.

Useful for situations when you have two endpoints using JSON 
that communicate similar data, however in slightly different formats. 

## Examples

Full code is in `examples` package.

#### [Example 1] Convert some formats
Input:

```
{"user":{"age": 30, "names": "Jack Oliver Jake", "lastname": "King"}, "profile": {"adclicks": [0,1,2,3]}}
{"user":{"age": 45, "names": "Emma Amelia Margaret", "lastname": "Smith"}, "profile": {"adclicks": [0,1]}}
{"user":{"age": 26, "names": "Thomas", "lastname": "Martin"}, "profile": {"adclicks": []}}
```

Conversion function:

```scala
val convert: JsValue => JsValue =
    asJsObject _ |> compose[JsObject](
        _.mapChild("user", _.asJsObject.rename("lastname", "surname")), // rename property
        _.mapDescendant("user.age", (j: JsValue) => j.asJsNumber.map(_ * 365)), // estimate age in days
        _.mapDescendant("user.names", (j: JsValue) => // convert names to array of strings
           JsArray(j.asString.split(' ').toVector.map(JsString(_))))
    )
```
      
Output:
```
{"user":{"age":10950,"names":["Jack","Oliver","Jake"],"surname":"King"},"profile":{"adclicks":[0,1,2,3]}}
{"user":{"age":16425,"names":["Emma","Amelia","Margaret"],"surname":"Smith"},"profile":{"adclicks":[0,1]}}
{"user":{"age":9490,"names":["Thomas"],"surname":"Martin"},"profile":{"adclicks":[]}}
```

#### [Example 2] Group keys into objects
Input:

```
{"name": "John", "mother_name": "Emma", "mother_age": 35, "father_name": "Thomas", "father_age": 40}
{"name": "Emma", "mother_name": "Olivia", "mother_age": 30, "father_name": "Jack", "father_age": 29}
```

Conversion function:

```scala
val groupParents: JsValue => JsValue =
    asJsObject _ |> compose[JsObject](
        _.groupAndRename(Map("mother_name" -> "name", "mother_age" -> "age"), "mother"),
        _.groupAndRename(Map("father_name" -> "name", "father_age" -> "age"), "father")
    )
```
      
Output:
```
{"mother":{"name":"Emma","age":35},"name":"John","father":{"name":"Thomas","age":40}}
{"mother":{"name":"Olivia","age":30},"name":"Emma","father":{"name":"Jack","age":29}}
```

#### [Example 3] Group keys into an array
Input:

```
{"bikeid": "RC4963", "owner1": "Peter", "owner2": "Amelia", "owner3": "Jack", "owner4": "John"}
{"bikeid": "RK2528", "owner1": "Thomas", "owner2": null, "owner3": "Jack", "owner4": "Margaret", "repairs": 4}
{"bikeid": "RC4694", "owner1": null, "owner2": null, "owner3": null, "owner4": null}
```

Conversion function:

```scala
val arrayize: JsValue => JsValue =
    asJsObject _ |> compose[JsObject](
        _.mapChildrenToArray(Vector("owner1", "owner2", "owner3", "owner4"), "owners"),
        _.mapChild("owners", _.asJsArray.filter(_ != JsNull)) // throw away empty values
    )
```
      
Output:
```
{"owners":["Peter","Amelia","Jack","John"],"bikeid":"RC4963"}
{"owners":["Thomas","Jack","Margaret"],"bikeid":"RK2528","repairs":4}
{"owners":[],"bikeid":"RC4694"}
```
