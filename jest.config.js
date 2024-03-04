module.exports = {
    testEnvironment: "jsdom",
    testMatch: ["**/src/test/resources/**/*.test.js"],
    verbose: true,
    preset: "ts-jest",
    moduleFileExtensions: ['js', 'json', 'jsx', 'ts', 'tsx', 'node'],
    setupFiles: [
        './setupJest.js'
    ],
};