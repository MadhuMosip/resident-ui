#!/bin/bash

# Angular Upgrade Script from v7 to v20
# This script will upgrade Angular incrementally through major versions

echo "Starting Angular upgrade from v7 to v20..."

# Step 1: Upgrade to Angular 9
echo "Upgrading to Angular 9..."
ng update @angular/core@9 @angular/cli@9 --force
npm install --legacy-peer-deps

# Step 2: Upgrade to Angular 10
echo "Upgrading to Angular 10..."
ng update @angular/core@10 @angular/cli@10 --force
npm install --legacy-peer-deps

# Step 3: Upgrade to Angular 11
echo "Upgrading to Angular 11..."
ng update @angular/core@11 @angular/cli@11 --force
npm install --legacy-peer-deps

# Step 4: Upgrade to Angular 12
echo "Upgrading to Angular 12..."
ng update @angular/core@12 @angular/cli@12 --force
npm install --legacy-peer-deps

# Step 5: Upgrade to Angular 13
echo "Upgrading to Angular 13..."
ng update @angular/core@13 @angular/cli@13 --force
npm install --legacy-peer-deps

# Step 6: Upgrade to Angular 14
echo "Upgrading to Angular 14..."
ng update @angular/core@14 @angular/cli@14 --force
npm install --legacy-peer-deps

# Step 7: Upgrade to Angular 15
echo "Upgrading to Angular 15..."
ng update @angular/core@15 @angular/cli@15 --force
npm install --legacy-peer-deps

# Step 8: Upgrade to Angular 16
echo "Upgrading to Angular 16..."
ng update @angular/core@16 @angular/cli@16 --force
npm install --legacy-peer-deps

# Step 9: Upgrade to Angular 17
echo "Upgrading to Angular 17..."
ng update @angular/core@17 @angular/cli@17 --force
npm install --legacy-peer-deps

# Step 10: Upgrade to Angular 18
echo "Upgrading to Angular 18..."
ng update @angular/core@18 @angular/cli@18 --force
npm install --legacy-peer-deps

# Step 11: Upgrade to Angular 19
echo "Upgrading to Angular 19..."
ng update @angular/core@19 @angular/cli@19 --force
npm install --legacy-peer-deps

# Step 12: Upgrade to Angular 20
echo "Upgrading to Angular 20..."
ng update @angular/core@20 @angular/cli@20 --force
npm install --legacy-peer-deps

echo "Angular upgrade completed! Current version:"
ng version

echo "Please review any breaking changes and update your code accordingly."

