#!/usr/bin/env bash


#!/bin/bash

# Set variables for the new account, database, and collection
resourceGroupName='tc'
name='attendee'
databaseName='attendee'

# Create a collection
az cosmosdb collection create \
	--collection-name 'user_social2' \
	--name $name \
	--db-name $databaseName \
	--resource-group $resourceGroupName
