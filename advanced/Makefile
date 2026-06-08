NPM=node_modules/.bin

.PHONY: install clean test test-advanced

$(NPM):
	npm install --no-save @percy/cli@^1.31.13

install: $(NPM)
	mvn dependency:resolve

clean:
	rm -rf target node_modules

# Run against the BrowserStack App Automate hub. Requires AA_USERNAME,
# AA_ACCESS_KEY, APP, PERCY_TOKEN env vars.
test test-advanced: install
	$(NPM)/percy app:exec -- mvn test
