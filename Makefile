NPM=node_modules/.bin
NPMDEPS=$(node_modules)

$(NPM): $(NPMDEPS)
	npm install

.PHONY: npm install clean serve test

install: $(NPM)

clean:
	rm -rf $$(cat .gitignore)

serve:
	mvn package
	java -cp target/example-percy-appium-java-1.0-SNAPSHOT.jar io.percy.examplepercyappiumjava.App

test-android: install
	$(NPM)/percy app:exec --  mvn compile exec:java -Dexec.mainClass="io.percy.examplepercyappiumjava.Android"

test-android-espresso: install
	$(NPM)/percy app:exec --  mvn compile exec:java -Dexec.mainClass="io.percy.examplepercyappiumjava.AndroidEspresso"

test-ios: install
	$(NPM)/percy app:exec --  mvn compile exec:java -Dexec.mainClass="io.percy.examplepercyappiumjava.Ios"
