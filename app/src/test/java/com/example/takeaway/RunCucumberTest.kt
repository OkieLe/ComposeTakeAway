package com.example.takeaway

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    tags = "not @ignored"
)
class RunCucumberTest
