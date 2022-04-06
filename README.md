![](logo.png)

# The Citadel Project

This is an educational project which states that software properly guarded by automated tests is a considerably more
sustainable and superior kind than software which lacks them. It can adopt any changes and have new features released to
production [safely](#release-safely) and [quickly](#release-quickly), it's always in a
[working state](#develop-efficiently) (no matter whether it is production or development) and anyone can start
contributing in [less than 5 minutes](#get-started). The mission is to give it a taste of what it's like to develop
software in that manner and our hope is that after that anyone tried it will consider this way to develop software as a
de-facto standard and will bring this experience to their production projects.

To get the idea of the application itself just check it out here http://34.118.91.30:8080.

# How to Make It Happen

Basically the only two principles have to be enforced:

* **Automated Tests.** Each code change must be fully covered with [automated tests](doc/testPyramid)
  . [No exceptions](.github/workflows/quality_gates.skip.reasons). Ensured by Code Review.
* **Quality Gates.** Automated tests are crucial, but they are not sufficient by itself. They must be run on each
  integration to the production branch, all the tests must be green, only in this case the change can be integrated.
  Ensured by [CI pipeline](.github/workflows/quality_gates.yml).

That's it, just two points. But it's naturally outcomes into: [quick start](#get-started),
[safe releases](#release-safely), [quick releases](#release-quickly), [stable working environment](#develop-efficiently)
, an environment welcome to [refactoring](#refactoring-is-welcome) and [experimenting](#experimenting-is-welcome).

## Get Started

To contribute or just to get a running instance locally all you need is SDK
([java](https://openjdk.java.net/projects/jdk/17/)), Containers Management Tool ([docker](https://www.docker.com/))
and Build Tool ([gradle](https://gradle.org/)). After that checkout the repository you are all set, just
execute `rebuildAndRun` gradle task (or for those who are not familiar with gradle
[rebuildAndRun.cmd](scripts/rebuildAndRun.cmd) is in place). Debug available via `rebuildAndRunDebug`.

## Why Does It Matter

### Release Safely

Guarded by the Quality Gates, the functionality already released is guaranteed not to be broken by a new drop.

### Release Quickly

Any change is ready for production right after it's integrated to production branch - no need to manually test it as
it's already covered by automated tests (Exploratory would be nice though, but still not a 'must have' - sense it
depending on the nature of the change). Regression testing? Yep, it's already done by the Quality Gates. You are good to
go.

### Develop Efficiently

The fact Quality Gates require the ability to be deployed from scratch on ephemeral environment (see
[System Tests](doc/testPyramid.md#system-tests)) leads to two positive side effects. The first is as it is guaranteed to
be deployable from scratch, then it's trivially to deploy locally. The second is the enforcement of so called 'as Code'
principles - Infrastructure as Code, Configuration as Code, etc. That means that there is no more headache to get proper
settings locally, chase of configuration drift and manual overrides - any attempt of manual intervention would cause
tests failures.

Besides that, application is stable at any point of time - you can pull the latest version of the main branch and all
the functionality expected to work - works, it's again guaranteed by the Automated Tests + Quality Gates pair.

### Refactoring is Welcome

It doesn't matter if you'd like just to clean up a class code a bit or fully re-design this messy module and finally
untangle this spaghetti ball into a clean and clear structure. You can act absolutely confidently in both cases as there
is no chance to bring any regression - it will be caught by the Quality Gates.

### Experimenting is Welcome

Pretty much similar to the previous point. It's time to upgrade to the latest Java version? A brand-new library promises
a lot of perks, but how much regression should we expect once it's in place? Just make a change and get near real-time
feedback on how many functions are broken. Oops, it seems you just have completed the PoC task. Don't tell your boss :)

## Based on

* [Robert C. Martin - The Principles of OOD (SOLID and more)](http://www.butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod)
* [Robert C. Martin - Clean Code: A Handbook of Agile Software Craftsmanship](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)
* [Kent Beck - Test Driven Development: By Example](https://www.amazon.com/Test-Driven-Development-Kent-Beck/dp/0321146530)
* [Steve Freeman - Growing Object-Oriented Software, Guided by Tests](https://www.amazon.com/Growing-Object-Oriented-Software-Guided-Tests/dp/0321503627) 
