import { readFileSync, writeFileSync } from 'node:fs';

const [listPath, readmePath] = process.argv.slice(2);
const START = '<!-- patches:start -->';
const END = '<!-- patches:end -->';

const readme = readFileSync(readmePath, 'utf8');
const start = readme.indexOf(START);
const end = readme.indexOf(END);
if (start < 0 || end < start) {
	console.log(`${readmePath} has no ${START} ... ${END} section, leaving it as it is`);
	process.exit(0);
}

const { patches } = JSON.parse(readFileSync(listPath, 'utf8'));
const cell = (text) => text.replaceAll('|', '\\|').replaceAll('\n', ' ');
const target = ({ compatibility }) =>
	compatibility.kind === 'packages'
		? compatibility.packages
				.map(({ package: name, versions }) => (versions?.length ? `\`${name}\` ${versions.join(', ')}` : `\`${name}\``))
				.join(', ')
		: 'Any app';

const groups = new Map();
for (const patch of patches.filter((p) => !p.hidden)) {
	const key = target(patch);
	groups.set(key, [...(groups.get(key) ?? []), patch]);
}

const tables = [...groups]
	.sort(([a], [b]) => a.localeCompare(b))
	.map(([key, group]) =>
		[
			`### ${key}`,
			'',
			'| Patch | What it does |',
			'|---|---|',
			...group.sort((a, b) => a.name.localeCompare(b.name)).map((p) => `| ${cell(p.name)} | ${cell(p.description ?? '')} |`),
		].join('\n'),
	)
	.join('\n\n');

writeFileSync(readmePath, `${readme.slice(0, start + START.length)}\n\n${tables}\n\n${readme.slice(end)}`);
