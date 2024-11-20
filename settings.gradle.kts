pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)  // Esto asegura que no se usen repositorios a nivel de proyecto
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MiClimaParcial"
include(":app")
