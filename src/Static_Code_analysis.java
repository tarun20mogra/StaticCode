
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Static_Code_analysis {
	
	static int class_count = 0;
	static int interface_count = 0;
	static int cmnts = 0;
	static int fun = 0;
	static int function = 0;
	static int variables_count = 0;
	static int interface_fun_count = 0;
	static ArrayList<String> arrayListInter = new ArrayList<String>();
	static ArrayList<String> arrayListFun = new ArrayList<String>();
	static ArrayList<String> variables_occurence = new ArrayList<String>();
	static Set<String> sample = new HashSet<String>();

	public static void main(String[] args) throws IOException {

		File folder = new File("C:/working/Adeshwar _accounts/src/adeshwar/accounts");
		File[] listOfFiles = folder.listFiles();
		// ArrayList<File> arrayList=new ArrayList<File>();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				String f = file.getPath();
				BufferedReader br = new BufferedReader(new FileReader(f));
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					class_count = classCount(line);
					interface_count = interfaceCount(line);
					fun = functionsCount(line);
					variables_count = variablesCount(line);

					if (interface_count > 0) {
						interface_fun_count = interfacefunCount(line);
					}

					if (line.contains("//") && line.contains("/*"))

					{
						cmnts++;

					}
					sb.append(line);
					sb.append("\n");
					line = br.readLine();
				}

			}

		}

		System.out.println("\tClasses=" + class_count + "\n\tInterface=" + interface_count + "\n\tcomments = " + cmnts);
		System.out.print("\tfuntion = " + fun);
		System.out.println("\n\tvariables = " + variables_count + "\n\tInterface methods=" + interface_fun_count);
		
		// System.out.println("arraylist"+variables_occurence.size()+"\t
		// sample"+sample.size());
		// overriden verification start
		if (arrayListInter.size() > 0) {
			for (int i = 0; i < arrayListInter.size(); i++) {
				String s = arrayListInter.get(i);
				int c = 0;

				if (arrayListFun.size() > 0) {

					for (int j = 0; j < arrayListFun.size(); j++) {
						String s1 = arrayListFun.get(j);

						if (s.trim().equals(s1.trim())) {
							c++;
						}
					}
					if (c == 0) {
						System.out.print(
								"\n Error:The Method '" + s.trim() + "' should be implement in the child class.\n");
								
					}
				} else {
					System.out
							.print("\n Error:The Method '" + s.trim() + "' should be implement in the child class.\n");
							
				}
			}
		}
		// overriden end
		// variable start
		if (variables_occurence.size() > 0) {

			for (int i = 0; i < variables_occurence.size(); i++) {
				String s = variables_occurence.get(i);

				int c = 0;

				if (sample.size() > 0) {
					Iterator itr = sample.iterator();
					while (itr.hasNext()) {
						String s1 = (String) itr.next();
						if (s1.trim().contains(s.trim())) {
							c++;

							// boolean
							// co=s1.matches("(?m)^(.)*System\\.out\\.print.*");

						}
					}
					if (c <= 1) {
						System.out.print("\nThe Variable '" + s + "' is not used any where.");
						
					}
					
					
				} else {
					System.out.print("The Variable '" + s + "' is not used");
					
				}
				// Naming convention

			}

		}

	}

	private static int interfacefunCount(String line) {

		if (!line.contains("//") && !line.contains("/*") && line.contains(";") && line.contains("(")) {
			if (line.contains("public") || line.contains("private") || line.contains("static")
					|| line.contains("protected")) {
				interface_fun_count++;
				String m = line;
				String[] method = m.split("\\(");
				String method2 = method[0];
				arrayListInter.add(method2);

			}
		}
		return interface_fun_count;
	}

	private static int interfaceCount(String line) {
		if (line.contains("interface")) {
			// suppose class name is interface1 or method name as interface1
			// etc. to handle those lines comments
			if (!line.contains(";") && !line.contains("class") && !line.contains("(") && !line.contains("//")
					&& !line.contains("/*")) {
				interface_count++;
				String string = line.replace("{", "");
				String[] parts = string.split("interface ");
				int n = parts.length;
				String part2 = parts[n - 1];
				if (!part2.trim().matches("^[A-Z]\\w*$")) {
					System.out.println("Verify Naming convention for interface :'" + part2 + "'\n");
					
				}
				

			}
		}
		return interface_count;
	}

	private static int classCount(String line) {

		if (line.contains("class")) {
			// suppose interface or method name or variable names as class1 to
			// handle those cases, comments as well
			if (!line.contains(";") && !line.contains("interface") && !line.contains("(") && !line.contains("//")
					&& !line.contains("/*")) {
				class_count++;

				String string = line.replace("{", "");
				String[] parts = string.split("class ");
				int n = parts.length;
				String part2 = parts[n - 1];
				String[] part3 = part2.split(" ");
				String cl = part3[0];

				if (!cl.trim().matches("^[A-Z]\\w*$")) {
					System.out.println("Verify Naming convention for class :'" + cl + "'\n");
				}
			}
		}
		return class_count;
	}

	private static int variablesCount(String line) {

		if (line.contains("int ") || line.contains("char ") || line.contains("String ") || line.contains("double ")
				|| line.contains("float ")) {
			if (!line.contains("(")) {
				String string = line.replace(";", "");
				String[] parts = string.split(" ");
				int n = parts.length;
				String part2 = parts[n - 1];

				if (line.contains(part2)) {
					if (part2.contains(",")) {
						String[] v = part2.split(",");

						variables_count = variables_count + v.length;
						// adding variables to list
						for (int k = 0; k < v.length; k++) {
							variables_occurence.add(v[k]);
							if (!v[k].matches("^[a-z_]\\w*$")) {
								System.out.println("\nVerify Naming convention for variable:'" + v[k] + "'");
								
							}
						}

					} else {
						variables_count++;
						variables_occurence.add(part2);
						if (!part2.matches("^[a-z_]\\w*$")) {
							System.out.println("\n Verify Naming convention for variable:'" + part2 + "'");
							
						}
						
					}
				}
			}
		}
		if (variables_count > 0) {
			for (int i = 0; i < variables_occurence.size(); i++) {
				String s = variables_occurence.get(i);
				if (!line.contains("//") && !line.contains("/*")) {
					if (line.contains("(") || line.contains(";"))
						if (line.contains(s)) {
							sample.add(line);
						}

				}

			}
		}
		return variables_count;
	}

	private static int functionsCount(String line) {

		if (!line.contains("//") && !line.contains("/*") && !line.contains(";") && line.contains("(")) {
			if (line.contains("public") || line.contains("private") || line.contains("static")
					|| line.contains("protected")) {
				fun++;

				// to verify overriden methods
				String m = line;
				String[] method = m.split("\\(");
				String method2 = method[0];
				arrayListFun.add(method2);
				String[] n = method2.split(" ");
				String fname = n[n.length - 1];
				if (!fname.trim().matches("^[a-z]\\w*$")) {
					System.out.println("Verify Naming convention for function or method :'" + fname + "' name\n");
					
				}

			}
		}
		return fun;
	}
}