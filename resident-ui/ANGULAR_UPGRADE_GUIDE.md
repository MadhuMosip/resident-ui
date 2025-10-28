# Angular Upgrade Guide: From v7 to v20

## Prerequisites

Before starting the upgrade process, ensure you have:

1. **Node.js Version**: Update to Node.js 20.19+ or 22.12+
   - Download from [nodejs.org](https://nodejs.org/)
   - Or use a version manager like nvm-windows

2. **Backup**: Create a backup of your project
3. **Git**: Ensure your project is under version control

## Current Status

Your project is currently at:
- Angular: 8.2.14 (partially upgraded from 7.2.0)
- Node.js: 20.15.0 (needs update to 20.19+)
- TypeScript: 3.5.3

## Upgrade Process

### Step 1: Update Node.js

```bash
# Download and install Node.js 20.19+ or 22.12+ from nodejs.org
# Or use nvm-windows:
nvm install 20.19.0
nvm use 20.19.0
```

### Step 2: Run the Upgrade Script

Use the provided batch file for Windows:

```bash
upgrade-angular.bat
```

Or run the commands manually:

```bash
# Upgrade to Angular 9
ng update @angular/core@9 @angular/cli@9 --force
npm install --legacy-peer-deps

# Upgrade to Angular 10
ng update @angular/core@10 @angular/cli@10 --force
npm install --legacy-peer-deps

# Continue through each version...
# Angular 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
```

## Major Breaking Changes to Expect

### Angular 8 → 9
- Ivy renderer becomes default
- TypeScript 3.6+ required

### Angular 9 → 10
- TypeScript 3.9+ required
- Angular Material updates

### Angular 10 → 11
- TypeScript 4.0+ required
- Angular Material updates

### Angular 11 → 12
- Ivy everywhere
- Legacy View Engine removed
- TypeScript 4.2+ required

### Angular 12 → 13
- TypeScript 4.4+ required
- Angular Material updates

### Angular 13 → 14
- TypeScript 4.6+ required
- Standalone components introduced

### Angular 14 → 15
- TypeScript 4.8+ required
- Standalone APIs stable

### Angular 15 → 16
- TypeScript 4.9+ required
- Angular Material updates

### Angular 16 → 17
- TypeScript 5.0+ required
- Control flow syntax introduced

### Angular 17 → 18
- TypeScript 5.2+ required
- Angular Material updates

### Angular 18 → 19
- TypeScript 5.4+ required
- Angular Material updates

### Angular 19 → 20
- TypeScript 5.6+ required
- Angular Material updates

## Post-Upgrade Tasks

1. **Update Dependencies**: Check for compatible versions of third-party libraries
2. **Fix Breaking Changes**: Address any compilation errors
3. **Update Tests**: Fix test configurations and specs
4. **Performance Testing**: Ensure the application still performs well
5. **Browser Testing**: Test in all supported browsers

## Troubleshooting

### Common Issues

1. **Peer Dependency Conflicts**: Use `--legacy-peer-deps` flag
2. **TypeScript Errors**: Update TypeScript version as required
3. **Build Failures**: Check for deprecated APIs and update accordingly

### Getting Help

- [Angular Update Guide](https://update.angular.io/)
- [Angular Migration Guide](https://angular.io/guide/migration)
- [Angular GitHub Issues](https://github.com/angular/angular/issues)

## Notes

- The upgrade process may take several hours
- Test thoroughly after each major version upgrade
- Keep your project under version control
- Consider upgrading in smaller increments if you encounter issues

