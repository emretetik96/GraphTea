// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/
package graphtea.plugins.reports.extension;

import graphtea.platform.core.BlackBoard;
import graphtea.plugins.main.GraphData;
import graphtea.ui.UIUtils;
import graphtea.ui.components.gpropertyeditor.GCellRenderer;
import graphtea.ui.components.utils.GFrameLocationProvider;
import graphtea.ui.extension.AbstractExtensionAction;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

/**
 * @author M. Ali Rostami - Conjecture check
 * @author Hooman Mohajeri Moghaddam - added save button, fixed recalculate button
 * @author azin azadi

 */
public class GraphReportExtensionAction extends AbstractExtensionAction {
	protected GraphReportExtension mr;
    public static boolean activeConjCheck = false;
	public static boolean connected = true;
	public static int Size = 9;
	public static boolean upto = false;
	public static boolean upperBound = true;
	public static boolean lowerBound = true;
	public static boolean strictLowerBound = true;
	public static boolean strictUpperBound = true;
	public static boolean iterative = false;
	CheckForAll cfa = new CheckForAll(mr);

	public static String currentType = "";


	JButton cont   = new JButton("Continue");

	public GraphReportExtensionAction(BlackBoard bb, GraphReportExtension gg) {
		super(bb, gg);
		this.mr = gg;
	}

	public String getParentMenuName() {
		return "Reports";
	}

	public Object performExtensionInCommandLine() {
		return mr.calculate(new GraphData(blackboard).getGraph());
	}

	Component rendererComponent;
	Component ContentPane;
	JDialog jd;
	JFileChooser fileChooser;
	public void performExtension() {
		//        if (testAndSetParameters(gr)) {
		new Thread() {

			public void run() {
                Object result;
				if(activeConjCheck && !mr.getName().equals("Bound Check")) {
                    cont.setEnabled(true);
					if(!iterative) {
						if (GraphReportExtensionAction.currentType != "") {
							result = cfa.forall();
						} else {
							result = cfa.forAllUnfiltered();
						}
					} else {
						CheckForAll cfa = new CheckForAll(mr);
						if (GraphReportExtensionAction.currentType != "") {
							result = cfa.forallIterative();
						} else {
							result = cfa.forAllUnfilteredIterative();
						}
					}

                } else {
                    cont.setEnabled(false);
                    result = mr.calculate(new GraphData(blackboard).getGraph());

                }
				if(result==null)
					return;
				jd = new JDialog(UIUtils.getGFrame(blackboard));
				jd.setVisible(true);
				jd.setTitle(mr.getName());
				jd.setLayout(new BorderLayout(3, 3));
				jd.add(new JLabel(mr.getDescription()), BorderLayout.NORTH);
				rendererComponent = GCellRenderer.getRendererFor(result);
				rendererComponent.setEnabled(true);
				jd.add(rendererComponent, BorderLayout.CENTER);
				JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
				JButton recalc = new JButton("Recalculate");
				panel.add(recalc, BorderLayout.SOUTH);
				panel.add(cont, BorderLayout.SOUTH);
				recalc.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent actionEvent) {
						Object result = mr.calculate(new GraphData(blackboard).getGraph());
						jd.remove(rendererComponent);
						rendererComponent = GCellRenderer.getRendererFor(result);
						rendererComponent.setEnabled(true);
						jd.add(rendererComponent, BorderLayout.CENTER);
						jd.pack();
						jd.repaint();

					}
				});

				cont.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent actionEvent) {
						String out = JOptionPane.showInputDialog("Please enter the counter " +
								"of the graph that you want to proceed.");
						int cnt = Integer.parseInt(out);
						new GraphData(blackboard).core.showGraph(cfa.getith(cnt));
					}
				});

				fileChooser = new JFileChooser();
				JButton save = new JButton("Save");
				panel.add(save);
				save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent actionEvent) {

						fileChooser.addChoosableFileFilter(new FileFilter() {

							@Override
							public String getDescription() {
								return "*.txt";
							}

							@Override
							public boolean accept(File arg0) {
								if (arg0.isDirectory()) 
									return true;
								else 
								{
									String path = arg0.getAbsolutePath().toLowerCase();
									if ((path.endsWith("txt") && (path.charAt(path.length() - 4)) == '.')) 
										return true;
								}
								return false;
							}
							});
						fileChooser.setDialogTitle("Choose a file");
						fileChooser.showSaveDialog(jd);
						try
						{

							File curFile = fileChooser.getSelectedFile();
							FileWriter fw = new FileWriter(curFile);
							Object result = mr.calculate(new GraphData(blackboard).getGraph());
							fw.write(result.toString());
							fw.close();
							JOptionPane.showMessageDialog(jd, "Saved to file successfuly.");

						}catch(Exception e)
						{
						}

						}
					});

				jd.add(panel, BorderLayout.SOUTH);
				jd.setLocation(GFrameLocationProvider.getPopUpLocation());
				jd.pack();

				}
			}.start();
		}

	}
