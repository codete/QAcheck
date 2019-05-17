## Tests engine


#### Dictionary
- **user app** - application name. Each application can contains multiple test cases.
- **test case** - test definition. Test cases are created automatically by api library when user uses _crawler_ or manually when user runs selenium tests. User can have multiple test cases which belong to one user application. Each _test case_ can contains multiple _test runs_.
- **test run** - execution of the _test case_. Each _test run_ can contains multiple _test steps_ - _test run_ is marked as passed if all _test steps_ which belongs to him passed.
- **test step** - one screenshot to compare. Currently multiple _test steps_ are created when user takes screenshot of scrollable page. Api library is scrolling through the whole page and creating multiple _test steps_ - one _test step_ per each screenshot.
- **test step config** - configuration which will be used to run screenshot comparator for _test step_ (one screenshot). 
- **comparison settings** - settings used by screenshot comparator. Contains i.a. _baseline screenshot path_ and _ignore areas_. When _test step_ comparison runs it creates snapshot of settings from _test step config_. Snapshot is created, because we want to keep history of settings modifications. For example, when user add new ignore areas to _test step_ we don't want to propagate this settings to all previous test runs.
- **comparison result** - result of comparison algorithm. Indicates if _test step_ passed/failed and contains paths to actual/diff screenshots.
- **user decision** - enum which represents user decision on GUI (accept current, accept baseline, no decision).
- **baseline screenshot** - screenshot which will be used as a referenced screenshot (last accepted, correct screenshot).
- **current screenshot** - screenshot taken during test, it will be compared to the baseline to calculate _comparison result_.
- **diff screenshot** - difference between the _baseline_ and _current_ screenshots. Based on _current screenshot_ with highlighted changes.
- **ignore areas** - areas on the screenshot which will be ignored during comparison. User can add multiple _ignore areas_ to the screenshot (using GUI). 

#### Engine logic
1. User uses api library to send screenshots which will be compared to the baseline.
2. If engine discovers that _test step_ is run for the first time it will initialize baseline and mark _test step_ as passed.
3. If engine discovers that _test step config_ for particular _test step_ exists (when this is not first run of the _test step_) it will be used by screenshot comparator.
4. Tests engine saves all screenshots sent to the server and also diff images if comparison algorithm found differences during comparison of _baseline_ and _current_ screenshot.
5. Screenshots are saved in the file system (you can configure root directory in the _application.yaml_ file).